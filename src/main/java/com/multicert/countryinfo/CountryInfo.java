/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.multicert.countryinfo;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.enterprise.context.RequestScoped;
import org.w3c.dom.Document;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * REST Web Service
 *
 * @author floriano.alves
 */
@Path("countryinfo")
@RequestScoped
public class CountryInfo {
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CountryInfo
     */
    public CountryInfo() {
    }

    /**
     * Retrieves representation of an instance of com.mycompany.countryinfo.CountryInfo
     * @return an instance of java.lang.String
     */
    @Path("{countryCode}")
    @GET
    @Produces("text/html")
    public String getHtml(@PathParam("countryCode") String countryCode) {
        String country = countryCode.toUpperCase();
        StringBuffer result = new StringBuffer ();
        StringBuffer httpResponse = new StringBuffer ();
        URL url;
        HttpURLConnection conn;
        DocumentBuilderFactory dbFactory;
        DocumentBuilder dBuilder;
        Document doc;
        Node nNode;
        Element eElement;
        BufferedReader br;
        String line;
        String capital = "Lisbon";
        String pais = "Portugal";
        String lat = "38.71667";
        String lng = "-9.13333";
        
        try {
            /** Informação do país */
            url = new URL (String.format ("http://api.geonames.org/countryInfo?lang=en&country=%s&username=faoa&style=full", country));
            System.out.println ("URI called: " + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod ("GET");
            conn.setRequestProperty ("Accept", "application/xml");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            
            try {
                br = new BufferedReader (new InputStreamReader (conn.getInputStream(), Charset.forName("UTF-8")));
                while ((line = br.readLine()) != null) {
                    httpResponse.append (line);
                }

                dbFactory = DocumentBuilderFactory.newInstance();
                dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(httpResponse.toString ().getBytes("utf-8"))));

                doc.getDocumentElement().normalize();
                nNode = doc.getFirstChild();
                eElement = (Element) nNode;

                result.append ("<h3>-Country Details-</h3>");
                result.append (eElement.getElementsByTagName("countryCode").item(0).getTextContent());
                result.append (": ");
                pais = eElement.getElementsByTagName("countryName").item(0).getTextContent();
                result.append (pais);
                result.append ("<br>Oficial Language: ");
                result.append (eElement.getElementsByTagName("languages").item(0).getTextContent());
                result.append ("<br>Population: ");
                result.append (eElement.getElementsByTagName("population").item(0).getTextContent());
                result.append ("<br>Currency: ");
                result.append (eElement.getElementsByTagName("currencyCode").item(0).getTextContent());
                result.append ("<br>Capital: ");
                capital = eElement.getElementsByTagName("capital").item(0).getTextContent();
                result.append (capital);
                result.append ("<br>North Latitude: ");
                result.append (eElement.getElementsByTagName("north").item(0).getTextContent());
                result.append ("<br>South Latitude: ");
                result.append (eElement.getElementsByTagName("south").item(0).getTextContent());
                result.append ("<br>West Longitude: ");
                result.append (eElement.getElementsByTagName("west").item(0).getTextContent());
                result.append ("<br>East Longitude: ");
                result.append (eElement.getElementsByTagName("east").item(0).getTextContent());
                
                conn.disconnect();
            }
            catch (IOException | RuntimeException | ParserConfigurationException | SAXException e) {
                result.append ("<h3>-Country Details-</h3>");
                result.append ("Information not found!");
                System.out.println ("Information not found!");
                System.out.println (e.getMessage ());
                conn.disconnect();
            }
                
            /** Coordenadas da Capital */
            url = new URL(String.format("http://api.geonames.org/search?lang=en&country=%s&name_equals=%s&maxRows=1&username=faoa", country, URLEncoder.encode(capital, "UTF-8")));
            System.out.println ("URI called: " + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/xml");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            try {
                br = new BufferedReader (new InputStreamReader (conn.getInputStream(), Charset.forName("UTF-8")));
                httpResponse = new StringBuffer ();
                while ((line = br.readLine()) != null) {
                    httpResponse.append (line);
                }

                dbFactory = DocumentBuilderFactory.newInstance();
                dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(httpResponse.toString ().getBytes("utf-8"))));

                doc.getDocumentElement().normalize();
                nNode = doc.getFirstChild();
                eElement = (Element) nNode;

                result.append ("<h3>-Capital Details-</h3>");
                result.append ("Capital Latitude: ");
                lat = eElement.getElementsByTagName("lat").item(0).getTextContent();
                result.append (lat);
                result.append ("<br>Capital Longitude: ");
                lng = eElement.getElementsByTagName("lng").item(0).getTextContent();
                result.append (lng);

                conn.disconnect();
            }
            catch (IOException | RuntimeException | ParserConfigurationException | SAXException e) {
                result.append ("<h3>-Capital Details-</h3>");
                result.append ("Information not found!");
                System.out.println ("Information not found!");
                System.out.println (e.getMessage ());
                conn.disconnect();
            }
            
            /** ICAO */
            url = new URL(String.format("http://api.geonames.org/findNearByWeatherXML?lang=en&lat=%s&lng=%s&username=faoa", lat, lng));
            System.out.println ("URI called: " + url);    
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/xml");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            try {
                br = new BufferedReader (new InputStreamReader (conn.getInputStream(), Charset.forName("UTF-8")));
                httpResponse = new StringBuffer ();
                while ((line = br.readLine()) != null) {
                    httpResponse.append (line);
                }

                dbFactory = DocumentBuilderFactory.newInstance();
                dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(httpResponse.toString ().getBytes("utf-8"))));

                doc.getDocumentElement().normalize();
                nNode = doc.getFirstChild();
                eElement = (Element) nNode;

                result.append ("<br>Capital ICAO: ");
                result.append (eElement.getElementsByTagName("ICAO").item(0).getTextContent());

                conn.disconnect();
            }
            catch (IOException | RuntimeException | ParserConfigurationException | SAXException e) {
                result.append ("<br>Capital ICAO: ");
                result.append ("Information not found!");
                System.out.println ("Information not found!");
                System.out.println (e.getMessage ());
                conn.disconnect();
            }
            
            /** Weather Info */
            url = new URL (String.format ("http://www.webservicex.net/globalweather.asmx/GetWeather?CityName=%s&CountryName=%s", URLEncoder.encode(capital, "UTF-8"), URLEncoder.encode(pais, "UTF-8")));
            System.out.println ("URI called: " + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/xml");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            try {
                br = new BufferedReader (new InputStreamReader (conn.getInputStream(), Charset.forName("UTF-8")));
                httpResponse = new StringBuffer ("<?xml version=\"1.0\" encoding=\"utf-16\"?>");
                br.readLine();
                line = br.readLine();
                if (!line.contains ("Data Not Found")) {
                    while ((line = br.readLine()) != null) {
                        httpResponse.append (line);
                    }
                    httpResponse.delete (httpResponse.length() - 9, httpResponse.length());

                    String httpResponseAux1 = httpResponse.toString().replaceAll ("&lt;", "<");
                    String httpResponseAux2 = httpResponseAux1.replaceAll ("&gt;", ">");

                    dbFactory = DocumentBuilderFactory.newInstance();
                    dBuilder = dbFactory.newDocumentBuilder();
                    doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(httpResponseAux2.getBytes("utf-16"))));

                    doc.getDocumentElement().normalize();
                    nNode = doc.getFirstChild();
                    eElement = (Element) nNode;

                    result.append ("<h3>-Capital Weather Information-</h3>");
                    result.append ("Wind: ");
                    result.append (eElement.getElementsByTagName("Wind").item(0).getTextContent());
                    result.append ("<br>Visibility: ");
                    result.append (eElement.getElementsByTagName("Visibility").item(0).getTextContent());
                    result.append ("<br>Sky Conditions: ");
                    result.append (eElement.getElementsByTagName("SkyConditions").item(0).getTextContent());
                    result.append ("<br>Temperature: ");
                    result.append (eElement.getElementsByTagName("Temperature").item(0).getTextContent());
                    result.append ("<br>Dew Point: ");
                    result.append (eElement.getElementsByTagName("DewPoint").item(0).getTextContent());
                    result.append ("<br>Relative Humidity: ");
                    result.append (eElement.getElementsByTagName("RelativeHumidity").item(0).getTextContent());
                    result.append ("<br>Pressure: ");
                    result.append (eElement.getElementsByTagName("Pressure").item(0).getTextContent());
                }
                else {
                    result.append ("<h3>-Capital Weather Information-</h3>");
                    result.append ("Information not found!");
                }

                conn.disconnect();
            }
            catch (IOException | RuntimeException | ParserConfigurationException | SAXException e) {
                result.append ("<h3>-Capital Weather Information-</h3>");
                result.append ("Information not found!");
                System.out.println ("Information not found!");
                System.out.println (e.getMessage ());
                conn.disconnect();
            }
            
            /** Informação Wikipedia do país */
            url = new URL (String.format ("http://api.geonames.org/wikipediaSearch?q=%s&maxRows=1&username=faoa", URLEncoder.encode(pais, "UTF-8")));
            System.out.println ("URI called: " + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod ("GET");
            conn.setRequestProperty ("Accept", "application/xml");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            
            try {
                httpResponse = new StringBuffer ();
                br = new BufferedReader (new InputStreamReader (conn.getInputStream(), Charset.forName("UTF-8")));
                while ((line = br.readLine()) != null) {
                    httpResponse.append (line);
                }

                dbFactory = DocumentBuilderFactory.newInstance();
                dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(httpResponse.toString ().getBytes("utf-8"))));

                doc.getDocumentElement().normalize();
                nNode = doc.getFirstChild();
                eElement = (Element) nNode;

                result.append ("<h3>-Country Wikipedia Information-</h3>");
                result.append ("Summary: ");
                result.append (eElement.getElementsByTagName("summary").item(0).getTextContent());
                result.append ("<br>Wikipedia Url: ");
                String wikiURL = eElement.getElementsByTagName("wikipediaUrl").item(0).getTextContent();
                result.append("<a href=\"").append(wikiURL).append("\">").append(pais).append ("</a>");
                String imagem = eElement.getElementsByTagName("thumbnailImg").item(0).getTextContent();
                result.append("<br><img src=\"").append(imagem).append("\" alt=\"").append(pais).append ("\"/>");
                
                conn.disconnect();
            }
            catch (IOException | RuntimeException | ParserConfigurationException | SAXException e) {
                result.append ("<h3>-Country Wikipedia Information-</h3>");
                result.append ("Information not found!");
                System.out.println ("Information not found!");
                System.out.println (e.getMessage ());
                conn.disconnect();
            }
        } catch (MalformedURLException e) {
              e.printStackTrace();
        } catch (IOException e) {
              e.printStackTrace();
        }
        
        System.out.println ("Resultado final: " + result);
        
        return "<html lang=\"en\"><body>" + result + "</body></html>";
    }

    /**
     * PUT method for updating or creating an instance of CountryInfo
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("text/html")
    public void putHtml(String content) {
    }
}
