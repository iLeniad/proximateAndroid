package com.example.leniad.prueba1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leniad.prueba1.data.loginDBHelper;
import com.example.leniad.prueba1.data.loginTabla;
import com.example.leniad.prueba1.data.loginTablaEsquema;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import android.app.Activity;

/**
 * A login screen that offers login via email/password.
 */



public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {


    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private UserPerfilTask mPerfilTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String auxSesion =pref.getString("sesion", null);
        editor.commit();
        if (auxSesion != null){

            Intent intent = new Intent(LoginActivity.this.getApplicationContext(), InicioActivity.class);

            LoginActivity.this.startActivityForResult(intent,0);

            finish();


        }



        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();



        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            HttpHandler sh = new HttpHandler();

            String url = "https://serveless.proximateapps-services.com.mx/catalog/dev/webadmin/authentication/login";

            // Making a request to url and getting response

            String jsonStr = sh.makeServiceCall(url);

            String TagFuncion = "inicioSesion";

            Log.e(TagFuncion, "Response from url: " + jsonStr);


            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node

                    loginDBHelper mloginDBHelper;



                    mloginDBHelper = new loginDBHelper(getBaseContext());

                    mloginDBHelper.insertar("delete from login");

                    String sql = "insert into login (success,message,error,id,token) values ('" + jsonObj.getString("success")+"','"+ jsonObj.getString("message")+"','"+ jsonObj.getString("error")+"','"+ jsonObj.getString("id")+"','"+ jsonObj.getString("token")+"')";

                    Log.e(TagFuncion,sql);

                    mloginDBHelper.insertar(sql);



                    return true;

                    // looping through All Contacts

                } catch (final JSONException e) {
                    Log.e(TagFuncion, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                    return false;

                }
            } else {
                Log.e(TagFuncion, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

                return false;

            }



        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {

                loginDBHelper mloginDBHelper = new loginDBHelper(getBaseContext());

                Cursor tabla = mloginDBHelper.query("select * from login");

                tabla.moveToFirst();

                Log.e("Termino login",tabla.getString(tabla.getColumnIndex("token")));

                String token = tabla.getString(tabla.getColumnIndex("token"));

                showProgress(true);
                mPerfilTask = new UserPerfilTask(token);
                mPerfilTask.execute((Void) null);



            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


    public class UserPerfilTask extends AsyncTask<Void, Void, Boolean> {


        private final String mtoken;


        UserPerfilTask(String token) {
            mtoken = token;

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            HttpHandler sh = new HttpHandler();

            String url = "https://serveless.proximateapps-services.com.mx/catalog/dev/webadmin/users/getdatausersession";

            // Making a request to url and getting response

            String jsonStr = sh.irPorPerfil(url,mtoken);

            String TagFuncion = "obteniendoPerfil";

            Log.e(TagFuncion, "Response from url: " + jsonStr);


            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node

                    JSONArray datos = jsonObj.getJSONArray("data");

                    loginDBHelper mloginDBHelper;



                    mloginDBHelper = new loginDBHelper(getBaseContext());

                    mloginDBHelper.insertar("delete from perfil");

                    String sql = "insert into perfil (id,nombres,apellidos,correo,numero_documento,ultima_sesion,eliminado,documentos_id,documentos_abrev,documentos_label,estados_usuarios_label) values ('" + datos.getJSONObject(0).getString("id")+"','"+ datos.getJSONObject(0).getString("nombres")+"','"+ datos.getJSONObject(0).getString("apellidos")+"','"+ datos.getJSONObject(0).getString("correo")+"','"+ datos.getJSONObject(0).getString("numero_documento")+"','"+ datos.getJSONObject(0).getString("ultima_sesion")+"','"+ datos.getJSONObject(0).getString("eliminado")+"','"+ datos.getJSONObject(0).getString("documentos_id")+"','"+ datos.getJSONObject(0).getString("documentos_abrev")+"','"+ datos.getJSONObject(0).getString("documentos_label")+"','"+ datos.getJSONObject(0).getString("estados_usuarios_label")+"')";

                    Log.e(TagFuncion,sql);

                    mloginDBHelper.insertar(sql);

                    mloginDBHelper.insertar("delete from secciones");

                    JSONArray secciones = datos.getJSONObject(0).getJSONArray("secciones");

                    for (int i=0; i < secciones.length(); i++) {
                        JSONObject auxDato = secciones.getJSONObject(i);


                        sql = "insert into secciones (id,seccion,abrev) values ('" + auxDato.getString("id")+"','"+ auxDato.getString("seccion")+"','"+ auxDato.getString("abrev")+"')";
                        Log.e(TagFuncion,sql);

                        mloginDBHelper.insertar(sql);


                    }



                    return true;

                    // looping through All Contacts

                } catch (final JSONException e) {
                    Log.e(TagFuncion, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                    return false;

                }
            } else {
                Log.e(TagFuncion, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

                return false;

            }



        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mPerfilTask = null;
            showProgress(false);

            if (success) {

                loginDBHelper mloginDBHelper = new loginDBHelper(getBaseContext());

                Cursor tabla = mloginDBHelper.query("select * from login");

                tabla.moveToFirst();



                String auxTexto = "id: "+tabla.getString(tabla.getColumnIndex("id")) + "\n"
                        + "success: " + tabla.getString(tabla.getColumnIndex("success")) + "\n"
                        + "error: " + tabla.getString(tabla.getColumnIndex("error")) + "\n"
                        + "message: " + tabla.getString(tabla.getColumnIndex("message")) + "\n"
                        + "token: " + tabla.getString(tabla.getColumnIndex("token")) + "\n";


                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Hola")
                        .setMessage(auxTexto)
                        .setCancelable(false)
                        .setPositiveButton(
                                "Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = pref.edit();

                                        editor.putString("sesion","1");

                                        editor.commit();

                                        Intent intent = new Intent(LoginActivity.this.getApplicationContext(), InicioActivity.class);

                                        LoginActivity.this.startActivityForResult(intent,0);

                                        finish();


                                    }
                                }).show();







            } else {

            }
        }

        @Override
        protected void onCancelled() {
            mPerfilTask = null;
            showProgress(false);
        }
    }


}

