package com.cherryclaw.portioncat;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Cat is the object class for Cat objects.  It contains instance variables
 * and methods for calculating the cat's age given it's birthday.
 *<p>
 * This class makes it easy to pass along Cat information to the database
 * in {@link com.cherryclaw.portioncat.AddCat}, and easy to access cat information
 * without calling {@link com.cherryclaw.portioncat.CatOpenHelper}
 * <p>
 * Parcelable is also implemented for future implementations.
 *
 * @author      Jocelynne Gonzales
 */


public class Cat  implements Parcelable {

    private int id;
    private String name;
    private double weight;
    private String birthday;
    private String foodType;
    private byte[] catImage;
    private String sex;

    /**
     * Class constructor
     */
    public Cat() {
    }

    /**
     * Class constructor specifying number of objects to create with id
     */
    public Cat(int id, String name, double weight, String birthday, String sex, String foodType, byte[] catImage) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.birthday = birthday;
        this.foodType = foodType;
        this.catImage = catImage;
        this.sex = sex;
    }


    /**
     * Class constructor specifying number of objects without id
     */
    public Cat(String name, double weight, String birthday, String sex, String foodType, byte[] catImage) {
        this.name = name;
        this.weight = weight;
        this.birthday = birthday;
        this.foodType = foodType;
        this.sex = sex;
        this.catImage = catImage;
    }


    /* Not supported at this time, but for when the user does not select an image */
    public Cat(String name, double weight, String birthday, String sex, String foodType) {
        this.name = name;
        this.weight = weight;
        this.birthday = birthday;
        this.foodType = foodType;
        this.sex = sex;
    }


    /**
     * Class constructor implementing parcelable
     */
    public Cat(Parcel in){
        readFromParcel(in);
    }
    public static final Parcelable.Creator<Cat> CREATOR
            = new Parcelable.Creator<Cat>() {

        public Cat createFromParcel(Parcel in) {
            return new Cat(in);
        }

        public Cat[] newArray(int size) {
            return new Cat[size];
        }
    };

    /* Parcelable methods */
    public int describeContents() {return 0;}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeDouble(weight);
        dest.writeString(birthday);
        dest.writeString(foodType);
        dest.writeString(sex);
        dest.writeByteArray(catImage);
    }

    private void readFromParcel(Parcel in ) {
        this.id = in.readInt();
        this.name = in.readString();
        this.weight = in.readDouble();
        this.birthday = in.readString();
        this.foodType = in.readString();
        this.sex = in.readString();
        this.catImage = new byte[in.readInt()];
        in.readByteArray(this.catImage);

    }



    /**
     * Returns the cat's age and calls on calcAge.
     * <p>
     * Splits the birthday in format "mm/dd/yyyy" and parses it to call on calcAge
     *
     * @return      Cat's age in years
     */
    public int getAge(){
        String[] date = birthday.split("/");

        int month = Integer.parseInt(date[0]);
        int day = Integer.parseInt(date[1]);
        int year = Integer.parseInt(date[2]);
        int age = calcAge(year,month,day);
        return age;

    }

    /**
     * Calculates the cat's age given parameters
     *
     * @param  yearIn  cat's year of birth, e.g. "2014"
     * @param  monthIn cat's month of birth, e.g. "03"
     * @param  dayIn cat's day of birth, e.g. "14"
     * @return      cat's age in years only]
     */
    public int calcAge (int yearIn, int monthIn, int dayIn) {


        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(yearIn, monthIn, dayIn);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal.get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if(a < 0)
            throw new IllegalArgumentException("Age < 0");
        return a;
    }




    /* Getters and setters */
    public void setID(int id) {this.id = id;}
    public int getID() {return this.id;}

    public void setName(String name) {this.name = name;}
    public String getName() {return this.name;}

    public void setWeight(double weight) {this.weight = weight;}
    public double getWeight() {return this.weight;}

    public void setSex(String sex) {this.sex = sex;}
    public String getSex() {return this.sex;}

    public void setBirthday(String birthday) {this.birthday = birthday;}
    public String getBirthday() {return this.birthday;}

    public void setFoodType(String foodType) {this.foodType = foodType;}
    public String getFoodType() {return this.foodType;}

    public void setCatImage(byte[] catImage){this.catImage = catImage;}
    public byte[] getCatImage() {return this.catImage;}

}
