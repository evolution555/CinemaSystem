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

public class AdminController extends Controller{
    private FormFactory formFactory;
    private Environment env;

    @Inject
    public AdminController(FormFactory f, Environment e){
        this.formFactory = f;
        this.env = e;
    }

    public Result adminFilm() {
        User u = HomeController.getUserFromSession();
        List<Film> allFilms = Film.findAll();
        return ok(adminFilm.render(u, allFilms, env));
    }

    public Result adminAddFilm(){
        Form<Film> addFilmForm = formFactory.form(Film.class);
        User u = HomeController.getUserFromSession();
        return ok(adminAddFilm.render(addFilmForm, u, null));
    }

    public Result addFilmSubmit(){
        Form<Film> newFilmForm = formFactory.form(Film.class).bindFromRequest();
        String saveImageMsg;
        if(newFilmForm.hasErrors()){
            return badRequest(adminAddFilm.render(newFilmForm, HomeController.getUserFromSession(), "Error with Form"));
        }

        List<Film> allFilms = Film.findAll();
        Film f = newFilmForm.get();
        for(Film film : allFilms){
            if(film.getTitle().equals(f.getTitle())){
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
    public Result updateMovie(String title){
        Film f;
        Form<Film> filmForm;
        try{
            f = Film.find.byId(title);
            filmForm = formFactory.form(Film.class).fill(f);
        }catch(Exception ex){
            return badRequest("error");
        }
        return ok(adminAddFilm.render(filmForm, HomeController.getUserFromSession(), null));
    }

    public Result deleteMovie(String title){
        Film.find.ref(title).delete();
        flash("success", "Movie has been deleted.");
        return redirect(routes.AdminController.adminFilm());
    }

    public String saveFile(String movieTitle, FilePart<File> uploaded){
        if(uploaded != null) {
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

}
