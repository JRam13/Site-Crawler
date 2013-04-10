package crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkFinder {

	private static String url, toFind;
	private static ArrayList<String> unvisitedURLs = new ArrayList<String>();
	private static ArrayList<String> listLinks = new ArrayList<String>();
	private static ArrayList<String> visitedURLs = new ArrayList<String>();
	private static String currentURL;
	private static boolean over500, over1000, over1500, over2000;

	public static void main(String[] args) throws IOException {
		
		Scanner scn = new Scanner(System.in); 
		System.out.println("Input url you wish to crawl (please include http://...):");
		url = scn.nextLine();
		System.out.println("Input the url (or word) you wish to find:");
		toFind = scn.nextLine();
		System.out.println("Searching... Please be patient");
		
		
		//System.out.println(url);
		unvisitedURLs.add(url);
		
		try {
			returnURLs(unvisitedURLs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Unvisited sites: "+unvisitedURLs.size());
			if(unvisitedURLs.size() != 0){
				currentURL = unvisitedURLs.remove(0);
				returnURLs(unvisitedURLs);
			}
			else{
				System.out.println("Done 1");
				System.out.println("Number of Visited Sites: "+visitedURLs.size());
				System.out.println("Number of Results: "+listLinks.size());
				printArray(listLinks);
				System.exit(0);
			}
		}
		over500 = false;
		over1000 = false;
		over1500 = false;
		over2000 = false;

	}

	private static void returnURLs(ArrayList<String> unvisitedURLs) throws IOException {
		currentURL = null;
		Document doc = null;
		
		while(!unvisitedURLs.isEmpty()){
			currentURL = unvisitedURLs.remove(0);
			visitedURLs.add(currentURL);
			doc = fetchHTML();
			
			//add url to unvisited if it has not been visited yet
			Elements links = null;
			try {
				links = doc.select("a[href]");
			} catch (Exception e) {
				
				if(unvisitedURLs.size() != 0){
					currentURL = unvisitedURLs.remove(0);
					returnURLs(unvisitedURLs);
				}
				else{
					System.out.println("Done 2");
					//printArray(visitedURLs);
				}
				
			}
			try {
				for (Element element : links) {
					String absHref = element.attr("abs:href"); 
					if(isUnique(absHref)){
						unvisitedURLs.add(absHref);
						if(linkFound(absHref)){
							listLinks.add(toFind + " Was Found at : " + currentURL + " ("+absHref+")");
						}
					}
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				
				if(unvisitedURLs.size() != 0){
					currentURL = unvisitedURLs.remove(0);
					returnURLs(unvisitedURLs);
				}
				else{
					System.out.println("Done 3");
					//printArray(visitedURLs);
					System.out.println("Number of Visited Sites: "+visitedURLs.size());
					printArray(listLinks);
					System.exit(0);
					
				}
				
			}
			status();
			
		}
		
		
		
		
		
		
	}

	private static void status() {
		
		if(unvisitedURLs.size() > 500){
			if(!over500){
				System.out.println("Processing over 500 links... Please wait");
				over500 = true;
			}
			else if(!over1000){
				System.out.println("Processing over 1000 links... Please wait");
				over1000 = true;
			}
			else if(!over1500){
				System.out.println("Processing over 1500 links... Please wait");
				over1500 = true;
			}
			else if(!over2000){
				System.out.println("Processing over 2000 links... Please wait");
				over2000 = true;
			}
		}
			
	}
	
	private static boolean linkFound(String url2) {
		if(url2.contains(toFind)){
			return true;
		}
		return false;
		
		
	}
	
	private static void printArray(ArrayList<String> urlList) throws IOException {
		for (int i = 0; i < urlList.size(); i++) {
			System.out.println(urlList.get(i));
			
		}
	}

	private static Document fetchHTML() throws IOException{
		Document doc = null;
		try {
			if(isInternal()){
				return doc = Jsoup.connect(currentURL).get();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(unvisitedURLs.size() != 0){
				currentURL = unvisitedURLs.remove(0);
				returnURLs(unvisitedURLs);
			}
			else{
				System.out.println("Done 4");
				//printArray(visitedURLs);
			}
			
		}
		return doc;
	}
	
	private static boolean isUnique(String url2){
		for (int i = 0; i < unvisitedURLs.size(); i++) {
			//System.out.println("URL Curent: " +url2);
			//System.out.println("URL :"+ visitedURLs.get(i));
			if(url2.equals(unvisitedURLs.get(i)) || url2.contains("#") || url2.contains(".pdf") || url2.contains(".jpg")
					|| url2.contains("%") || url2.contains("@")){ //don't want any anchors, pdf, img, @

				return false;
			}
		}
		
		for (int i = 0; i < visitedURLs.size(); i++) {
			//System.out.println("URL Curent: " +url2);
			//System.out.println("URL :"+ visitedURLs.get(i));
			if(url2.equals(visitedURLs.get(i))){

				return false;
			}
		}
		
		
		return true;
		
	}
	
	private static boolean isInternal(){
		if(currentURL.contains(url)){
			return true;
		}
		return false;
	}

}