package com.cherryclaw.portioncat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import static com.cherryclaw.portioncat.DbBitmapUtility.getBytes;


/**
 * AddCat contains Camera and file storage features, it also contains a button to cancel user input.
 * All information must be entered correctly or else the application might crash.
 * <p>
 * At this time, editing cat attributes is not supported but will be in the future.
 *
 * @author Jocelynne Gonzales
 * @see SQLiteOpenHelper
 * @see SQLiteDatabase
 */

public class AddCat extends AppCompatActivity {

    /* Number codes for Image Requests*/
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 555;
    private static int REQUEST_IMAGE_CAPTURE = 888;
    private static int PICK_IMAGE_REQUEST = 444;


    private static final String TAG = "AddCat";
    private static String food;

    //Set byte array for non-photo picking support
    private static byte[] pickedImage;


    //Default value is male
    private static String sex = "male";

    /**
     * Contains listeners to add a photo, take a photo, cancel an add entry, toggle the gender,
     * select a food type through radio buttons and the finish button.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cat);

        /* Buttons and EditText fields */
        RadioGroup foodType = (RadioGroup) findViewById(R.id.radioGroup1);
        final EditText catName = (EditText) findViewById(R.id.editText5);
        final EditText catBirthday = (EditText) findViewById(R.id.editText);
        final EditText catWeight = (EditText) findViewById(R.id.editText7);
        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton2);
        final Button bAddPhoto = (Button) findViewById(R.id.button);
        final Button bTakePhoto = (Button) findViewById(R.id.button4);

        Button bFinished = (Button) findViewById(R.id.button2);
        final Button bCancel = (Button) findViewById(R.id.button3);



        /* Cat is male by default, if toggle is pushed it will be female */
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sex = "female";
                } else {
                    sex = "male";
                }
            }
        });


        /* Listener for the ADD PHOTO button*/
        View.OnClickListener addPhotoButton = new View.OnClickListener() {
            public void onClick(View v) {
                //Request permissions to access external storage
                if (ContextCompat.checkSelfPermission(AddCat.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // For showing an explanation
                    if (ActivityCompat.shouldShowRequestPermissionRationale(AddCat.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    } else {
                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(AddCat.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                    }
                }
                Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhotoIntent, PICK_IMAGE_REQUEST);
            }
        };


        /* Take a photo from the phone's camera, launches camera */
        bTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });


        /* Cancel adding a cat and finish the activity */
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });


        /* Finish button listener makes sure the cat's name is between 1-10 characters long.
         * Creates cat object based on user input, then inserts object to the database. */
        bFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Makes sure cat is 1-10 chars long, and an image is chosen or taken.
                if (catName.getText().toString().length() > 10 || catName.getText().toString().length() < 1) {
                    Toast.makeText(getApplicationContext(), "Label name must be 1-10 characters long", Toast.LENGTH_SHORT).show();
                } else if (pickedImage == null) {
                    Toast.makeText(getApplicationContext(), "You must choose an image", Toast.LENGTH_SHORT).show();


                } else {
                    //Everything is filled out, go ahead and add the cat to the database
                    CatOpenHelper db = new CatOpenHelper(getApplicationContext());

                    //Create cat object
                    Cat myCat = new Cat(
                            catName.getText().toString(),
                            Double.valueOf(catWeight.getText().toString()),
                            catBirthday.getText().toString(),
                            sex,
                            food,
                            pickedImage
                    );


                    db.addCat(myCat);
                    //Close activity
                    sex = "male"; //Fixes a bug where the gender is not reset
                    finish();
                }
            }
        });





        /* When radio button is selected, the option is saved in str */
        foodType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonWet:
                        food = "Wet";
                        //do stuff
                        break;
                    case R.id.radioButtonDry:
                        food = "Dry";
                        break;
                }
            }
        });

        /* Set add photo listener */
        bAddPhoto.setOnClickListener(addPhotoButton);

    }

    /**
     * Dispatches intent to take a photo
     *
     * @see MediaStore
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        ImageView catPhoto = (ImageView) findViewById(R.id.image_cat);

        /* If user took photo with camera*/
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = imageReturnedIntent.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            catPhoto.setImageBitmap(imageBitmap);

            //Set cat photo with camera photo
            pickedImage = getBytes(imageBitmap);
        }

        /* If user selected an image from internal storage */
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {

            Uri selectedImage = imageReturnedIntent.getData();


            //Store file
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            //Set cat photo thumbnail with selected bitmap
            catPhoto.setImageBitmap(bitmap);

            //Store bitmap as byte array to add to the database
            pickedImage = getBytes(bitmap);


        }

    }


    /* Used to disable functionality after */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                   //Disable functionality that depends on permission
                }
                return;
            }
        }
    }
}