package com.javacodegeeks.android.googlemaps;

public class GlobalMercator {
	static int tileSize = 256;
	static double initialResolution = 2 * Math.PI * 6378137 / tileSize;
	//156543.03392804062 for tileSize 256 pixels
	static double  originShift = 2 * Math.PI * 6378137 / 2.0;

	public static double LatToMeters(double lat){
		double my = Math.log( Math.tan((90 + lat) * Math.PI / 360.0 )) / (Math.PI / 180.0);

		my = my * originShift / 180.0;
		
		return my; 
		
	}
	
	public static double LonToMeters(double lon){
		double mx = lon * originShift / 180.0;
		return mx;
	}
	
	public static double LatToPixels(double lat,int zoom)
	{

		double res = Resolution( zoom );
		double py = (lat + originShift) / res;
		return py;
	}

	public static double LonToPixels(double lon,int zoom)
	{

		double res = Resolution( zoom );
		double px = (lon + originShift) / res;
		return px;
	}
	
	public static double Resolution(int zoom )
	{
		//# return (2 * math.pi * 6378137) / (self.tileSize * 2**zoom)
		return initialResolution / Math.pow(2, zoom);
	}

}
