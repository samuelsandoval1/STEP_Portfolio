// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public final class FindMeetingQuery {   

    private static int MAX_DURATION = 60 * 24;

    // Returns sorted TimeRanges that have attendees in the attendees set.
    private static ArrayList<TimeRange> getBusyTimeRangesForAttendees(Collection<Event> events, Collection<String> mandatoryAttendees) {   
      Set<String> attendeesSet = new HashSet<String>();
      for(String attendee : mandatoryAttendees) {
        attendeesSet.add(attendee);
      }

      ArrayList<TimeRange> timeRangesWithAttendees = new ArrayList <TimeRange> ();
      for(Event event: events) {
        Collection<String> eventAttendees = event.getAttendees();
        TimeRange when = event.getWhen();
        for(String attendee : eventAttendees) {   
          if(attendeesSet.contains(attendee)) {
            timeRangesWithAttendees.add(when);
            break;
          } 
        }
      }
      Collections.sort(timeRangesWithAttendees, TimeRange.ORDER_BY_START);
      return timeRangesWithAttendees;
    }

    //Calculates the availbleTimes from the series of events
    private static ArrayList <TimeRange> findAvailableTimes(Collection<Event> events, Collection<String> mandatoryAttendees, long duration) {
      ArrayList<TimeRange> availableTimeForAll = new ArrayList<TimeRange>();
      int start = TimeRange.START_OF_DAY;

      if(mandatoryAttendees.isEmpty()){
          availableTimeForAll.add(TimeRange.WHOLE_DAY);
          return availableTimeForAll;
      }
    
      if (duration > MAX_DURATION) {
        return availableTimeForAll;
      }
      if(events.isEmpty()) {
        availableTimeForAll.add(TimeRange.WHOLE_DAY);
        return availableTimeForAll;
      }

      ArrayList<TimeRange> timeRangeWithAttendees = getBusyTimeRangesForAttendees(events, mandatoryAttendees);
      for(TimeRange when : timeRangeWithAttendees) {
        int meetingStart = when.start();
        if(meetingStart < start) {
            start = Math.max(start, when.end());
            continue;            
        }
       
        if(start + duration <= meetingStart) {
        availableTimeForAll.add(TimeRange.fromStartEnd(start, meetingStart, false));
        }
        start = when.end();
      }
      int end = TimeRange.END_OF_DAY;
      if(start + duration <= end) {
        availableTimeForAll.add(TimeRange.fromStartEnd(start, end, true));
      }

      return availableTimeForAll;
    }
    
    public Collection <TimeRange> query(Collection <Event> events, MeetingRequest request) {
      long duration = request.getDuration();
      Collection<String> mandatoryAttendees = request.getAttendees();
      ArrayList<TimeRange> availableTimeForAll = findAvailableTimes(events, mandatoryAttendees, duration);
      return availableTimeForAll;              
    }
}