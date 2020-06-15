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
import java.util.Set;

public final class FindMeetingQuery {   

    private static ArrayList <TimeRange>  findAvailableTimes(Collection<Event> events, Collection<String> mandatoryAttendees,long duration) {
      ArrayList<TimeRange> availableTimeForAll = new ArrayList<TimeRange>();
      int end = TimeRange.END_OF_DAY;
      int start = TimeRange.START_OF_DAY;

      if(mandatoryAttendees.isEmpty()){
          availableTimeForAll.add(TimeRange.WHOLE_DAY);
          return availableTimeForAll;
      }
    
      if (duration > 60 * 24) {
        return availableTimeForAll;
      }

      Set<String> attendeesSet = new HashSet<String>();
      for(String attendee : mandatoryAttendees) {
        attendeesSet.add(attendee);
      }
          
      if(events.isEmpty()) {
        availableTimeForAll.add(TimeRange.WHOLE_DAY);
        return availableTimeForAll;
      }
     
      for(Event event: events) {
        Collection<String> eventAttendees = event.getAttendees();
        TimeRange when = event.getWhen(); 
        boolean attendeeInEvent = false;
        for(String attendee : eventAttendees) {   
            if(attendeesSet.contains(attendee)) {
                attendeeInEvent = true;
                break;
            } 
        }
        if(!attendeeInEvent) {
            continue;
        }
        
        int meetingStart = when.start();
        if(meetingStart < start) {
            if(start > when.end()) {
                continue;
            }
            else {
            start = when.end();
            continue;
            }
        }

        if(start + duration <= meetingStart) {
        availableTimeForAll.add(TimeRange.fromStartEnd(start, meetingStart, false));
        }
        start = when.end();

      }
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