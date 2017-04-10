package controllers;

import models.Booking;
import models.Payments;
import models.users.Login;
import models.users.User;
import play.api.Environment;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.booking;
import views.html.payment;
import views.html.signUp;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class PaymentController extends Controller {
    private FormFactory formFactory;
    private Environment env;

    @Inject
    public PaymentController(Environment e, FormFactory f) {
        this.env = e;
        this.formFactory = f;
    }

    public Result payment() {
        DynamicForm newPaymentForm = formFactory.form();
        User u = HomeController.getUserFromSession();
        String error = null;
        Booking b = null;
        return ok(payment.render(b, newPaymentForm, u, env, error));
    }

    public Result paymentSubmit() {
        DynamicForm newPaymentForm = formFactory.form().bindFromRequest();
        DynamicForm errorForm = formFactory.form().bindFromRequest();
        User u = HomeController.getUserFromSession();
        Booking b = null;
        String name = newPaymentForm.get("name");
        String cardNo = newPaymentForm.get("cardNumber");
        if(newPaymentForm.get("expMonth") == "Month"){
            flash("error", "Please select a valid expirey month..");
            return redirect(routes.HomeController.index());
        }
        String month = newPaymentForm.get("expMonth");
        int year = Integer.parseInt(newPaymentForm.get("expYear"));
         if(newPaymentForm.get("expYear") == "Year"){
             flash("error", "Please select a valid expirey year.");
             return redirect(routes.HomeController.index());
        }
        int cvv = Integer.parseInt(newPaymentForm.get("cvv2"));

        Payments pay = new Payments(name, cardNo, month, year, cvv);
        pay.save();
        flash("success", "Payment Successful enjoy your movie.");
        return redirect(routes.HomeController.index());
    }
}
