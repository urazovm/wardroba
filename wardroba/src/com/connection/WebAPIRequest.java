package com.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.util.Log;

public class WebAPIRequest{
	public Document performGet(String url) 
	{
		Document doc = null;
		try 
		{
			DefaultHttpClient client = new DefaultHttpClient();
			URI uri = new URI(url);
			
			//String encodedurl = URLEncoder.encode(url);
			
			HttpGet method = new HttpGet(uri);
			HttpResponse res = client.execute(method);
			InputStream data = res.getEntity().getContent();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(data);
			
		} catch (ClientProtocolException e) 
		{
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (URISyntaxException e) 
		{
		
			System.out.println("dd"+e);
		} 
		
		
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return doc;
	}
}
