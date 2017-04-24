package controllers;

import play.api.Environment;
import play.db.ebean.Transactional;
import play.mvc.*;
import views.html.adminPages.*;
import play.data.*;
import models.users.*;
import models.*;

import javax.inject.Inject;
import java.nio.file.Files;
import java.util.*;

import play.mvc.Http.MultipartFormData.FilePart;

import java.io.*;

@Security.Authenticated(Secured.class)
@With(AuthAdmin.class)
public class AdminController extends Controller {
    private FormFactory formFactory;
    private Environment env;

    @Inject
    public AdminController(FormFactory f, Environment e) {
        this.formFactory = f;
        this.env = e;
    }

    public Result adminFilm() {
        User u = HomeController.getUserFromSession();
        List<Film> allFilms = Film.findAll();
        List<carousel> allCarosels = carousel.findAll();
        return ok(adminFilm.render(u, allFilms, env, allCarosels));
    }


    public Result adminAddFilm() {
        Film f = new Film();
        User u = HomeController.getUserFromSession();
        return ok(adminAddFilm.render(f, u, null));
    }

    public Result addFilmSubmit() {
        DynamicForm df = formFactory.form().bindFromRequest();
        Film f = new Film();
        f.setTitle(df.get("title"));
        f.setDirector(df.get("director"));
        f.setTrailerURL(df.get("trailerURL"));
        f.setSummery(df.get("summery"));
        try {
            f.setDuration(Integer.parseInt(df.get("duration")));
        }catch(NumberFormatException e){
            return badRequest(adminAddFilm.render(f, HomeController.getUserFromSession(), "Duration must be a number"));
        }

        String saveImageMsg;

        List<Film> allFilms = Film.findAll();
        for (Film film : allFilms) {
            if (film.getTitle().equals(f.getTitle())) {
                return badRequest(adminAddFilm.render(f, HomeController.getUserFromSession(), "Movie already in database."));
            }
        }
        f.save();

        Http.MultipartFormData data = request().body().asMultipartFormData();
        FilePart image = data.getFile("upload");

        flash("success", saveFile(f.getFilmId(), image));
        return redirect(routes.AdminController.adminFilm());
    }

    public Result updateMovie(String movie){
        Film f = Film.find.byId(movie);
        return ok(adminUpdateFilm.render(f, HomeController.getUserFromSession(), null));
    }

    public Result updateMovieSubmit(){
        DynamicForm df = formFactory.form().bindFromRequest();
        String title = df.get("title");
        String director = df.get("director");
        String summary = df.get("summery");
        String trailer = df.get("trailerURL");
        String id = df.get("id");

        Film f = Film.find.byId(id);
        int duration = 0;
        try{
            duration = Integer.parseInt(df.get("duration"));
        }catch (NumberFormatException e){
            return badRequest(adminUpdateFilm.render(f, HomeController.getUserFromSession(), "Duration must be a number"));
        }
        if(title.equals("")){
            return badRequest(adminUpdateFilm.render(f, HomeController.getUserFromSession(), "Title cannot be blank."));
        }

        f.setTitle(title);
        f.setDirector(director);
        f.setTrailerURL(trailer);
        f.setSummery(summary);
        f.setDuration(duration);
        f.update();


        flash("success", "Movie Updated");
        return redirect(routes.AdminController.adminFilm());
    }


    public Result deleteMovie(String title) {
        Film f = Film.find.byId(title);
        Film.find.ref(title).delete();
        flash("success", "Movie has been deleted.");

        //Deleting image from folder.
        File file = new File("public/images/FilmPosters/" + f.getFilmId() + ".jpg");
        file.delete();
        return redirect(routes.AdminController.adminFilm());
    }

    public String saveFile(String movieTitle, FilePart<File> uploaded) {
        if (uploaded != null) {
            String filename = uploaded.getFilename();
            String extension = "";

            String mimeType = uploaded.getContentType();

            if (mimeType.startsWith("image/")) {
                int i = filename.lastIndexOf('.');
                if (i >= 0) {
                    extension = filename.substring(i + 1);
                }

                File file = uploaded.getFile();
                file.renameTo(new File("public/images/FilmPosters/" + movieTitle + "." + extension));
            }
            return "Movie Added";
        }
        return "no file";
    }

    //Showings
    public Result adminShowing(String title) {
        User u = HomeController.getUserFromSession();
        List<Showing> showingsList = Showing.findMovieShowings(title);
        Film f = Film.find.byId(title);
        //List<Showing> showingsList = Showing.findAll();
        return ok(adminShowing.render(u, showingsList, f));
    }

    //Add showings to particular film
    public Result adminAddShowing(String title) {
        User u = HomeController.getUserFromSession();
        Film f = Film.find.byId(title);

        //List<Showing> showingsList = Showing.findMovieShowings(title);

        return ok(adminAddShowing.render(u, f));
    }

    public Result adminShowingSubmit() {
        User u = HomeController.getUserFromSession();
        DynamicForm newShowingForm = formFactory.form().bindFromRequest();

        Film f = null; //Delete this and use redirect instead of return ok.
        if (newShowingForm.hasErrors()) {
            return ok(adminAddShowing.render(u, f)); // val "a" is a place holder for film title val.
        }

        Showing s = null;
        List<Showing> showings = Showing.findAll();
        for (Showing show : showings) {
            if (show.getDate().equals(newShowingForm.get("date")) && show.getTitle().equals(newShowingForm.get("title"))) {
                s = show;
            }
        }

        if (s == null) {
            s = new Showing(Integer.parseInt(newShowingForm.get("screen")), newShowingForm.get("date"),
                    newShowingForm.get("title")); //newShowingForm.get();
        }

        if (s.getShowingId() != null) {
            s.addShowing(newShowingForm.get("time"));
        }
        return redirect(routes.AdminController.adminFilm());
    }

    //need to fill html form with showing details. As hits null pointer
    @Transactional
    public Result updateShowing(String id) {
        User u = HomeController.getUserFromSession();
        Form<Showing> newShowingForm;
        try {
            Showing s = Showing.find.ref(id);
            newShowingForm = formFactory.form(Showing.class).fill(s);
        } catch (Exception ex) {
            return badRequest("error");
        }
        return ok(adminAddShowing.render(HomeController.getUserFromSession(), null));
    }

    public Result deleteShowing(String id) {
        Showing.find.ref(id).delete();
        flash("success", "Showing has been deleted.");
        return redirect(routes.AdminController.adminFilm());
    }

    //Carousel
    public Result adminBanners() {
        User u = HomeController.getUserFromSession();
        List<Film> allFilms = Film.findAll();
        List<carousel> allCarousels = carousel.findAll();
        return ok(adminBanners.render(u, allFilms, env, allCarousels));
    }

    public Result adminAddCarousel() {
        Form<carousel> addCarouselForm = formFactory.form(carousel.class);
        User u = HomeController.getUserFromSession();
        return ok(adminAddCarousel.render(addCarouselForm, u, null));
    }

    public Result addCarouselSubmit() {
        Form<carousel> addCarouselForm = formFactory.form(carousel.class).bindFromRequest();
        if (addCarouselForm.hasErrors()) {
            return badRequest(adminAddCarousel.render(addCarouselForm, HomeController.getUserFromSession(), "Error with Form"));
        }

        List<carousel> allcarousels = carousel.findAll();
        carousel c = addCarouselForm.get();
        for (carousel carousel : allcarousels) {
            if (carousel.getTitle().equals(c.getTitle())) {
                c.update();
                routes.AdminController.adminAddCarousel();
            }
        }
        c.save();

        Http.MultipartFormData data = request().body().asMultipartFormData();
        FilePart image = data.getFile("upload");

        flash("success", "Banner Added");
        return redirect(routes.AdminController.adminFilm());
    }

    public Result deleteBanners(String title) {
        carousel.find.ref(title).delete();
        flash("success", "Banner has been deleted.");
        return redirect(routes.AdminController.adminFilm());
    }

    public Result adminMessages() {
        User u = HomeController.getUserFromSession();
        List<Messages> allMessages = Messages.findAll();
        return ok(adminMessages.render(u, env, allMessages));
    }

    public Result deleteMessages(String id) {
        Messages.find.ref(id).delete();
        flash("success", "Message has been deleted.");
        return redirect(routes.AdminController.adminMessages());
    }

    public static int getMessageCount() {
        return Messages.findAll().size();
    }
    public Result adminStaff() {
        User u = HomeController.getUserFromSession();
        List<Staff> allStaff = Staff.findAll();
        return ok(adminStaff.render(u, allStaff, env));
    }

    public Result adminAddStaff() {
        User u = HomeController.getUserFromSession();
        Form<Staff> addStaffForm = formFactory.form(Staff.class);
        return ok(adminAddStaff.render(addStaffForm, u, null));
    }


    public Result addStaffSubmit() {
        User u = HomeController.getUserFromSession();
        Form<Staff> newStaffForm = formFactory.form(Staff.class).bindFromRequest();
        if (newStaffForm.hasErrors()) {
            return badRequest(adminAddStaff.render(newStaffForm, u, null));
        }

        Staff newStaff = newStaffForm.get();

        if (newStaff.getId() == null) {
            newStaff.save();
        } else if (newStaff.getId() != null) {
            newStaff.update();
        }

        Http.MultipartFormData data = request().body().asMultipartFormData();
        FilePart staffImage = data.getFile("upload");

        flash("success", "Staff: " + newStaff.getName() + saveStaffFile(newStaff.getId(), staffImage));
        return redirect(routes.AdminController.adminStaff());

    }

    public String saveStaffFile(Long id, FilePart<File> upload) {
        if (upload != null) {
            String fileName = upload.getFilename();
            String ext = "";

            String mimeType = upload.getContentType();

            if (mimeType.startsWith("image/")) {
                int i = fileName.lastIndexOf('.');
                if (i >= 0) {
                    ext = fileName.substring(i + 1);
                }

                File file = upload.getFile();
                file.renameTo(new File("public/images/staffImages/" + id + "." + ext));
            }
            return " Has Been Added | Updated";
        }
        return "No File";
    }

    public Result adminDeleteStaff(Long id) {
        Staff.find.ref(id).delete();
        flash("success", "Staff has been Removed");
        return redirect(routes.AdminController.adminStaff());
    }


    @Transactional
    public Result adminUpdateStaff(Long id) {
        User u = HomeController.getUserFromSession();
        Staff s;
        Form<Staff> staffForm;
        try {
            s = Staff.find.byId(id);
            staffForm = formFactory.form(Staff.class).fill(s);
        } catch (Exception ex) {
            return badRequest("error");
        }
        return ok(adminAddStaff.render(staffForm, u, null));
    }


}


