package com.example.server.model;



import com.example.model.*;

import java.util.*;

public class MapManager {

        private static float MAP_WIDTH = 800f;
        private static float MAP_HEIGHT = 600f;
        //	private static float START_DISTANCE_FROM_EDGE = 30f;
        private static float AVATAR_HEIGHT = 50f;
        private static float AVATAR_WIDTH = 40f;

        // indices into the Float[]
        public static int X_VALUE = 0;
        public static int Y_VALUE = 1;
        public static int FACING_VALUE = 2;


        // use 4 corners as starting locations - this should change as
        // list should be GameDesignVars.MAX_PLAYERS_PER_GAME rows
        //		by 3 columns/datum  (x, y, initial facing)
        private static ArrayList<Float[]> startingLocations = null;

        /*
         *
        startingLocations
        {
            //	 	x		Y	FACING		CORNER OF SCREEN
            //	   ---     ---  ------ 		----------------
                { 64.0f,  536f, UP}, 		// LEFT, BOTTOM
                {  736f, 64.0f, DOWN}, 	// RIGHT, TOP
                { 64.0f, 64.0f, DOWN}, 	// LEFT, TOP
                {  736f,  536f, UP} 		// RIGHT, BOTTOM
            });


                // STUB:
    Polygon myCharacter = createPlayerAvatar(AVATAR_WIDTH, MAP_HEIGHT - AVATAR_HEIGHT, 90, AVATAR_COLORS[0]);
    Polygon otherCharacter = createPlayerAvatar(MAP_WIDTH - AVATAR_WIDTH, AVATAR_HEIGHT, 270, AVATAR_COLORS[1]);
    Polygon thirdCharacter = createPlayerAvatar(AVATAR_WIDTH, AVATAR_HEIGHT, 270, AVATAR_COLORS[2]);
    Polygon fourthCharacter = createPlayerAvatar(MAP_WIDTH - AVATAR_WIDTH, MAP_HEIGHT - AVATAR_HEIGHT, 90, AVATAR_COLORS[3]);

        private static int MAP_WIDTH = 800;
        private static int MAP_HEIGHT = 600;
        private static int START_DISTANCE_FROM_EDGE = 30;
        private static float AVATAR_HEIGHT = 50f;
        private static float AVATAR_WIDTH = 40f;

         */
        public static ArrayList<Float[]> getstartingLocations() {

            // only need to create startingLocations once
            if( MapManager.startingLocations == null) {

                MapManager.startingLocations = new ArrayList<Float[]>();
                MapManager.startingLocations.add(new Float[] {AVATAR_WIDTH, MAP_HEIGHT - AVATAR_HEIGHT, PlayerData.Facing.UP.getDirection()} );
                MapManager.startingLocations.add(new Float[] {MAP_WIDTH - AVATAR_WIDTH, AVATAR_HEIGHT, PlayerData.Facing.DOWN.getDirection()} );
                MapManager.startingLocations.add(new Float[] {AVATAR_WIDTH, AVATAR_HEIGHT, PlayerData.Facing.DOWN.getDirection()} );
                MapManager.startingLocations.add(new Float[] {MAP_WIDTH - AVATAR_WIDTH, MAP_HEIGHT - AVATAR_HEIGHT, PlayerData.Facing.UP.getDirection()} );
            }

            return MapManager.startingLocations;
        }


        public static boolean setPlayersStartingLocations(Collection<PlayerData> players ) {

            MapManager.getstartingLocations();		// initializes startingLocations

            Float [] nextLocation;
            int locationIndex = 0;
            for( PlayerData player : players ) {

                // if more players than startingLocations
                if( locationIndex >= MapManager.startingLocations.size() ) {

                    System.out.println("WARNING: ServerApplication.setPlayersStartingLocations() locationIndex = " +
                            locationIndex + " is greater than # of locations =  " + MapManager.startingLocations.size() +
                            "\n\tRepeating same locations previously used.");
                    locationIndex = 0;
                }


                nextLocation = MapManager.startingLocations.get(locationIndex);
                player.setX( nextLocation[X_VALUE] );
                player.setY( nextLocation[Y_VALUE] );
                player.getFacing().setDirection( nextLocation[FACING_VALUE] );

                ++locationIndex;
            }

            return true;
        }
}
