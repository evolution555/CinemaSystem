package controllers;

import play.api.Environment;
import play.db.ebean.Transactional;
import play.mvc.*;
import views.html.adminPages.*;
import play.data.*;
import models.users.*;
import models.*;

import javax.inject.Inject;
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
        Form<Film> addFilmForm = formFactory.form(Film.class);
        User u = HomeController.getUserFromSession();
        return ok(adminAddFilm.render(addFilmForm, u, null));
    }

    public Result addFilmSubmit() {
        Form<Film> newFilmForm = formFactory.form(Film.class).bindFromRequest();
        String saveImageMsg;
        if (newFilmForm.hasErrors()) {
            return badRequest(adminAddFilm.render(newFilmForm, HomeController.getUserFromSession(), "Error with Form"));
        }

        List<Film> allFilms = Film.findAll();
        Film f = newFilmForm.get();
        for (Film film : allFilms) {
            if (film.getTitle().equals(f.getTitle())) {
                f.update();
                routes.AdminController.adminFilm();
            }
        }
        f.save();

        Http.MultipartFormData data = request().body().asMultipartFormData();
        FilePart image = data.getFile("upload");

        flash("success", saveFile(f.getTitle(), image));
        return redirect(routes.AdminController.adminFilm());
    }

    @Transactional
    public Result updateMovie(String title) {
        Film f;
        Form<Film> filmForm;
        try {
            f = Film.find.byId(title);
            filmForm = formFactory.form(Film.class).fill(f);
        } catch (Exception ex) {
            return badRequest("error");
        }
        return ok(adminAddFilm.render(filmForm, HomeController.getUserFromSession(), null));
    }

    public Result deleteMovie(String title) {
        Film.find.ref(title).delete();
        flash("success", "Movie has been deleted.");
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
            return "Movie Added / Updated";
        }
        return "no file";
    }

    //Showings
    public Result adminShowing(String title) {
        User u = HomeController.getUserFromSession();
        List<Showing> showingsList = Showing.findMovieShowings(title);
        //List<Showing> showingsList = Showing.findAll();
        return ok(adminShowing.render(u, showingsList, title));
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
        for(Showing show : showings){
            if(show.getDate().equals(newShowingForm.get("date")) && show.getTitle().equals(newShowingForm.get("title"))){
                s = show;
            }
        }

        if(s == null) {
            s = new Showing(Integer.parseInt(newShowingForm.get("screen")), newShowingForm.get("date"),
                    newShowingForm.get("title")); //newShowingForm.get();
        }

        if (s.getShowingId() != null) {
            s.addShowing(newShowingForm.get("time"));
        }
        return redirect(routes.AdminController.adminFilm());
    }
    //need to fill html form with showing details.
    @Transactional
    public Result updateShowing(String title) {
        User u = HomeController.getUserFromSession();
        Film f;
        Form<Film> filmForm;
        try {
            f = Film.find.byId(title);
            filmForm = formFactory.form(Film.class).fill(f);
        } catch (Exception ex) {
            return badRequest("error");
        }
        return ok(adminAddShowing.render(HomeController.getUserFromSession(), null));
    }
    //Needs on delete cascade
    public Result deleteShowing(String id) {
        Showing.find.ref(id).delete();
        flash("success", "Showing has been deleted.");
        return redirect(routes.AdminController.adminFilm());
    }

    //Carousel
    public Result adminBanners(){
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
}


