package com.example.wardroba;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import com.ImageLoader.ImageLoader;
import com.connection.Constants;
import com.connection.JSONfunctions;
import com.connection.WebAPIHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileEditActivity extends Fragment
{
	ImageLoader imageLoader;
	ProgressBar imgLoader1;
	ImageView imgProfilePhoto;
	TextView img_addPhoto;
	Button btnSave;
	Bitmap profileBitmap;
	private JSONObject json;
	private static boolean isImageChange=false;
	private static boolean isPasswordSet=false;
	public static int IMAGE_SELECTOR=101;
	public EditText edtName,edtSurname,edttCityAddress,edtEmail,edtPassword,edtRepatPassword;
	Typeface tf;
	public final Pattern email_pattern= Pattern.compile
				(  "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
              + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
              + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
              + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
              + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
              + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
				);
	public void setResponseFromRequest(int request,int user_id,String msg)
	{
		if(user_id!=0)
		{
			//Toast.makeText(getActivity(), "Update successfully.", Toast.LENGTH_SHORT).show();
			if(isPasswordSet)
			{
				String password=edtPassword.getText().toString();
				String repeatPassword=edtRepatPassword.getText().toString();
				String URL= Constants.CHANGE_PASSWARD_URL+"&id="+Constants.REGISTER_USERID+"&password="+password+"&cpassword="+repeatPassword;
				Log.d("update Passward URL= ", URL.toString());
				JSONfunctions jsonCall=new JSONfunctions(ProfileEditActivity.this, "Please wait...", Constants.edit_profile_change_password);
				jsonCall.execute(URL);
			}
			else
			{
				if(isImageChange)
				{
					new ImageUploadTask().execute();
				}
				Constants.IS_PROFILE_CHANGED=true;
			}
		}
	}
	public void setResponseChangePassword(JSONObject jArray)
 	 {
 		 json=(JSONObject)jArray;
 		 String response;
 		 if(json!=null)
 		 {
 			 try {
 				 response=json.getString("success").toString();
				 if(response.equals("true"))
	 			 {
	 				 //Toast.makeText(getActivity(), "Profile update successfully", Toast.LENGTH_SHORT).show();
	 				 Constants.LOGIN_USERID=Integer.valueOf(json.getString("id").toString());
	 			 }
				 else
				 {
					 //Toast.makeText(getActivity(), "Profile update successfully", Toast.LENGTH_SHORT).show();
				 }
				 if(isImageChange)
				{
					new ImageUploadTask().execute();
				}
				 Constants.IS_PROFILE_CHANGED=true;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 			
 			 
 		 }
 	 }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.profile_edit_activity, null);
		
		tf= Typeface.createFromAsset(getActivity().getAssets(),"fonts/GOTHIC.TTF");
        imageLoader=new ImageLoader(getActivity());
        imgLoader1=(ProgressBar)root.findViewById(R.id.progLoader);
        imgProfilePhoto=(ImageView)root.findViewById(R.id.img_profile);
        btnSave=(Button)root.findViewById(R.id.btn_save);
        img_addPhoto=(TextView)root.findViewById(R.id.img_add_profile);
        edtName=(EditText)root.findViewById(R.id.edt_name);
        edtSurname=(EditText)root.findViewById(R.id.edt_surname);
        edttCityAddress=(EditText)root.findViewById(R.id.edt_city);
        edtEmail=(EditText)root.findViewById(R.id.edt_email);
        edtEmail.setEnabled(false);
        edtPassword=(EditText)root.findViewById(R.id.edt_passward);
        edtRepatPassword=(EditText)root.findViewById(R.id.edt_repeat_passward);
        isImageChange=false;
        isPasswordSet=false;
        Bundle bundle=getArguments();
        edtName.setText(bundle.getString("name"));
        edtSurname.setText(bundle.getString("surname"));
        edttCityAddress.setText(bundle.getString("city"));
        edtEmail.setText(bundle.getString("Email"));
        imageLoader.DisplayImage(bundle.getString("profileImage"), imgProfilePhoto, imgLoader1);
        btnSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String update_url;
				
				if(!checkEmpty())
				{
					
					
					String name,surname,city,email,password,repeatPassword;
					name=edtName.getText().toString();
					surname=edtSurname.getText().toString();
					city=edttCityAddress.getText().toString();
					password=edtPassword.getText().toString();
					repeatPassword=edtRepatPassword.getText().toString();
					if((password.length()==0) && (repeatPassword.length()==0))
					{
						update_url=Constants.PROFILE_SAVE_URL+"id="+Constants.LOGIN_USERID+"&name="+name+"&lastname="+surname+"&city="+city;
						WebAPIHelper apiHelper=new WebAPIHelper(Constants.edit_profile, ProfileEditActivity.this, "Please wait...");
						Log.d("UpdateProfile", "url:"+update_url);
						apiHelper.execute(update_url);
						isPasswordSet=false;
					}
					else if(!password.equals(repeatPassword))
					{
						Toast.makeText(getActivity(), "Please verify 'Password' and 'Repeat Password'", Toast.LENGTH_SHORT).show();
					}
					else
					{
						isPasswordSet=true;
						//http://dev.wardroba.com/serviceXml/updateprofile.php?id=15&name=Nilesh&lastname=Pambhar&city=Rajkot&new_password=nilesh&confirm_password=nilesh
						WebAPIHelper apiHelper=new WebAPIHelper(Constants.edit_profile, ProfileEditActivity.this, "Please wait...");
						update_url=Constants.PROFILE_SAVE_URL+"id="+Constants.LOGIN_USERID+"&name="+name+"&lastname="+surname+"&city="+city+"&new_password="+password+"&confirm_password="+repeatPassword;
						Log.d("UpdateProfile", "url:"+update_url);
						apiHelper.execute(update_url);
					}
				}
				else
				{
					Toast.makeText(getActivity(), "Please enter all values", Toast.LENGTH_SHORT).show();
				}
			}
		});
        img_addPhoto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				selectImage();
			}
		});

		return root;
	}
	
	 private void selectImage() 
	 {
	
		 	final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle("Add Photo!");
	        builder.setItems(options, new DialogInterface.OnClickListener() {

	            @Override

	            public void onClick(DialogInterface dialog, int item) 
	            {

	                if (options[item].equals("Take Photo"))
	                {

	                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	                    startActivityForResult(intent, 1);
	                }
	                else if (options[item].equals("Choose from Gallery"))
	                {
	                	Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	                    startActivityForResult(intent, 2);
	                }
	                else if (options[item].equals("Cancel")) 
	                {
	                	dialog.dismiss();
	                }

	            }

	        });

	        builder.show();

	    }
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
				if(resultCode==Activity.RESULT_OK)
				{
					switch (requestCode) {
					case 1: // from camera
						 Log.d("Camera", "Picture captured:"+data.getData());
						 //Uri selectedImage = data.getData();
						 Bitmap bitmapImage = (Bitmap) data.getExtras().get("data");
					     imgProfilePhoto.setImageBitmap(bitmapImage);
					     profileBitmap=bitmapImage;
					     isImageChange=true;
						break;
					case 2: // from gallery
						 Uri selectedImage1 = data.getData();
						 String path=getPath(selectedImage1);
						 Log.d("ImageUrl", "Image upload:"+path);
					     imgProfilePhoto.setImageURI(selectedImage1);
					     profileBitmap=BitmapFactory.decodeFile(path);
					     isImageChange=true;
						break;
					
					default:
						break;
					}
				}
    }
	
	 public String getPath(Uri uri) 
	 {

	        String[] projection = { MediaStore.Images.Media.DATA };
	        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        cursor.moveToFirst();
	        return cursor.getString(column_index);
    }
	 
	 class ImageUploadTask extends AsyncTask<Void, Void, String> {
		 private String webAddressToPost = "http://dev.wardroba.com/serviceXml/userimage.php?id="+Constants.LOGIN_USERID;

		 // private ProgressDialog dialog;
		 private ProgressDialog dialog = new ProgressDialog(getActivity());

		 @Override
		 protected void onPreExecute() {
		  dialog.setMessage("Please wait...");
		  dialog.show();
		 }

		 @Override
		 protected String doInBackground(Void... params) {
		  try {
		   HttpClient httpClient = new DefaultHttpClient();
		   HttpContext localContext = new BasicHttpContext();
		   HttpPost httpPost = new HttpPost(webAddressToPost);

		   MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

		   ByteArrayOutputStream bos = new ByteArrayOutputStream();
		   Bitmap scaleBitmap=Bitmap.createScaledBitmap(profileBitmap, profileBitmap.getWidth()/2, profileBitmap.getHeight()/2, true);
		   profileBitmap=(Bitmap)scaleBitmap;
		   profileBitmap.compress(CompressFormat.JPEG, 50, bos);
		   byte[] data = bos.toByteArray();
		   String file = Base64.encodeToString(data, Base64.DEFAULT);
		   ByteArrayBody byteBody=new ByteArrayBody(data, "my_photo.jpg");
		   entity.addPart("filename", byteBody);
		   

		   httpPost.setEntity(entity);
		   HttpResponse response = httpClient.execute(httpPost,localContext);
		   BufferedReader reader = new BufferedReader(new InputStreamReader(
		     response.getEntity().getContent(), "UTF-8"));

		   String sResponse = reader.readLine();
		   Log.e("HttpResponse", "Response:"+response);
		   return sResponse;
		  } catch (Exception e) {
		   // something went wrong. connection with the server error
			  e.printStackTrace();
		  }
		  return null;
		 }

		 @Override
		 protected void onPostExecute(String result) {
		  dialog.dismiss();
		  
		  Toast.makeText(getActivity(), "file uploaded",Toast.LENGTH_LONG).show();
		 }
		}
	public boolean checkEmpty()
	{
		String name,surname,city,email;
		name=edtName.getText().toString();
		surname=edtSurname.getText().toString();
		city=edttCityAddress.getText().toString();
		email=edtEmail.getText().toString();
		
		if(name.equals("") || surname.equals("") || city.equals("") ||email.equals(""))
		{
			return true;
		}
		else {
			return false;
		}
	}
	public boolean isValidEmail(String email)
	{
		if(email.matches(email_pattern.pattern()))
		{
			return true;
		}
		return false;
				
	}
	public void onAttach(Activity activity) 
	{
    	ImageView btnBack;
		btnBack=(ImageView)activity.findViewById(R.id.btnBack);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				
				getFragmentManager().popBackStack();
			}
		});
		super.onAttach(activity);
   }
//	@Override
//	public void onStart() {
//		// TODO Auto-generated method stub
//		super.onStart();
//		Bundle arguments=getArguments();
//	}
}