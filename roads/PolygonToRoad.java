package polytoroads;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.util.PointList;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


public class PolygonToRoad
{




    // Polyline generating library setup
    //
    private static GraphHopper gh = new GraphHopper().forDesktop();
    static {
        gh.load("switzerland");
    }



    //
    //
    public static ArrayList<Double[][]> wiglePath(double xLat,double xLon,double yLat,double yLon,int meters){

        ArrayList<Double[][]> candidates = new ArrayList<Double[][]>();

        double step = 0.000018; // ~ degrees in 1 meter;

        double dx = step * Math.abs(yLat - xLat) / Math.sqrt( Math.pow((yLon-xLon),2)+Math.pow((yLat-xLat),2));
        double dy = step * Math.abs(yLon-xLon) / Math.sqrt( Math.pow((yLon-xLon),2)+Math.pow((yLat-xLat),2));

        double[] shiftsX = new double[meters*2+1];
        double[] shiftsY = new double[meters*2+1];

        int j=0;
        for (int i=-meters;i<=meters;i++){
            shiftsX[j] = dx* (double)i;
            shiftsY[j] = dy*(double)i;
            j++;
        }

        for(int i=0;i<meters*2+1;i++)
            for(int k=0;k<meters*2+1;k++){
                Double[] f = new Double[2];
                f[0] = xLat+shiftsX[i];
                f[1] = xLon+shiftsY[i];

                Double[] t = new Double[2];
                t[0] = yLat+shiftsX[k];
                t[1] = yLon+shiftsY[k];

                Double[][] candidate = new Double[2][];
                candidate[0] = f;
                candidate[1] = t;

                candidates.add(candidate);
            }

        return candidates;
    }



    public static void main( String[] args ) throws Exception
    {

        // Counter
        int i = 0;



        // Setup the CSV scanner
        //
        Scanner s = new Scanner(new File("output_demo_file.csv"));
        s.useDelimiter("(?i),|\r?\n");



        // Setup the output file
        //
        BufferedWriter out = new BufferedWriter(new FileWriter(new File("roads_processed_frame.js")), 32768);
        out.write("roads = [\n");




        while (s.hasNext()){

            // Read the line from CSV file
            //
            String id = s.next();
            System.out.print(id+"|");
            double xLatitude = Double.parseDouble(s.next());
            System.out.print(xLatitude+"|");
            double xLongitude = Double.parseDouble(s.next());
            System.out.print(xLongitude+"|");
            double yLatitude = Double.parseDouble(s.next());
            System.out.print(yLatitude+"|");
            double yLongitude = Double.parseDouble(s.next());
            System.out.print(yLongitude+"|");
            int speed = Integer.parseInt(s.next());
            System.out.print(speed+"\r\n");


            // Look for the fastest root using GraphHopper and graph generated from OSM map of Switzerland
            //
            ArrayList<Double[][]> candidates = wiglePath(xLatitude,xLongitude,yLatitude,yLongitude,15);

            double minPath = 9999999.0;
            PointList minPathPoints = new PointList();

            for(Double[][] candidate :candidates){
                xLatitude = candidate[0][0];
                xLongitude = candidate[0][1];

                yLatitude = candidate[1][0];
                yLongitude = candidate[1][1];

                GHRequest request = new GHRequest(xLatitude,xLongitude,yLatitude,yLongitude);
                request.algorithm("dijkstrabi");
                GHResponse response = gh.route(request);

                double rDistance = response.time();
                if (rDistance < minPath){
                    minPath = rDistance;
                    minPathPoints = response.points();
                }
            }

            out.write("\t{id:"+id+",");
            out.write("path:"+"\""+minPathPoints+"\",");
            out.write("start:["+xLatitude+","+xLongitude+"],");
            out.write("end:["+yLatitude+","+yLongitude+"],");
            out.write("speed:"+speed+"},\n");

            System.out.println(i++);
            out.flush();


        }

        out.close();

    }
}
