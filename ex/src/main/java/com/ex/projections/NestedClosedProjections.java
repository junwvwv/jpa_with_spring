package com.ex.projections;

public interface NestedClosedProjections {

    String getUsername();
    TeamInfo getTeam();

    interface TeamInfo {

        String getName();

    }

}
