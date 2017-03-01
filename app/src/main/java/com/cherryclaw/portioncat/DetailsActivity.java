package com.cherryclaw.portioncat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import static com.cherryclaw.portioncat.DbBitmapUtility.getImage;

/**
 * DetailsActivity is launched when a respective cat in the listview is selected.  This is where
 * portions are shown and calculated, along with basic cat vitals.  More features are planned to
 * be added in the following month.
 *
 * @author Jocelynne Gonzales
 */

public class DetailsActivity extends Activity {
    String myCatName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ImageView catPhoto = (ImageView) findViewById(R.id.imageView);

        TextView tvCatName = (TextView) findViewById(R.id.textView);
        TextView tvWeight= (TextView) findViewById(R.id.textView6);
        TextView tvAge = (TextView) findViewById(R.id.textView13);
        TextView tvSex = (TextView) findViewById(R.id.textView12);

        TextView tvCatCalorieOverview = (TextView) findViewById(R.id.textView17);
        TextView tvFoodType = (TextView) findViewById(R.id.cat_food_detail);
        TextView tvFoodPerMeal = (TextView) findViewById(R.id.cat_meal);
        TextView tvFoodPerDay = (TextView) findViewById(R.id.cat_day);

        //Edit button is currently not functioning
        Button bEdit = (Button) findViewById(R.id.button6);




        //Open database and get catName from intent
        final CatOpenHelper handler = new CatOpenHelper(this);
        String catName = getIntent().getStringExtra("catName");

        myCatName = catName;

        Log.d("DetailsActivity", catName);
        //Query for the cat object
        Cat cat = handler.findCat(catName); //Cat object found, values can now be accessed.
        tvCatName.setText(catName); //set name
        tvAge.setText(Integer.toString(cat.getAge()) + " years old"); //set age
        tvSex.setText(cat.getSex()); //set gender

        /* Set photo from database*/
        catPhoto.setImageBitmap(getImage(cat.getCatImage()));


        /* Set weight in 0.00 format*/
        NumberFormat formatter = new DecimalFormat("#0.00");
        String fWeight = formatter.format(cat.getWeight());
        tvWeight.setText(fWeight +" lbs");


        /* Set calorie overview */
        tvCatCalorieOverview.setText(cat.getName()+
                " needs to consume " +
                formatter.format(calculateCalories(cat.getWeight()))+ " calories per day.");


        /*  If cat eats wet food, use wet formula.
        *   If cat eats dry food, use dry formula.
        */
        if (cat.getFoodType().equals("Wet") ) {
            tvFoodType.setText("Wet Food");
            double temp = calculateWet(cat.getWeight()); //Wet formula
            /* Set text */
            tvFoodPerDay.setText(formatter.format(temp)+ " oz per day");
            tvFoodPerMeal.setText(formatter.format(temp/2)+ " oz per meal");

        } else if (cat.getFoodType().equals("Dry")){

            tvFoodType.setText("Dry Food");
            double temp1 = calculateDry(cat.getWeight()); //Dry formula
            /* Set text */
            tvFoodPerDay.setText(formatter.format(temp1)+ " cups per day");
            tvFoodPerMeal.setText(formatter.format(temp1/2)+ " cups per meal");
        }
    }

    /**
     * Formula for calculating dry food
     *
     * @param  weight  Cat's weight with type double
     * @return         Portion in cups per day for cat
     */
    protected double calculateDry(double weight){
        double portionInCups = (calculateCalories(weight))/300.00;
        return portionInCups;
    }

    /**
     * Formula for calculating caloric needs per day
     *
     * @param  weight  Cat's weight with type double
     * @return         Calories of type double
     */
    protected double calculateCalories(double weight){
        return 30.00*weight;
    }

    /**
     * Formula for calculating wet food
     *
     * @param  weight  Cat's weight with type double
     * @return         Portion in ounces per day for cat
     */
    protected double calculateWet(double weight){
        double portionInOz  = ((calculateCalories(weight))/250.00)*6;
        return portionInOz;
    }


    /**
     * removeCat is called when the user presses the "delete" button.  It alerts them if they
     * wish to delete the cat.  If they select "no" the deletion is canceled.  If yes, the cat
     * is deleted and user returns to {@link com.cherryclaw.portioncat.MainActivity}
     *
     * @param  view    of type View, used by XML onclick attribute.
     * @see AlertDialog
     */
    public void removeCat (View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
        builder.setMessage("Are you sure you wish to delete "+ myCatName+"?");
        builder.setCancelable(true);

        final CatOpenHelper handler = new CatOpenHelper(this);
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        handler.deleteCat(myCatName);
                        dialog.cancel();
                        finish();
                    }
                });

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
