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
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public final class FindMeetingQuery {        

    public Collection <TimeRange> query(Collection <Event> events, MeetingRequest request) {
      long duration = request.getDuration();
      Collection<String> mandatoryAttendees = request.getAttendees();

      ArrayList<TimeRange> availableTimeForAll = new ArrayList<TimeRange>();

      
      if(mandatoryAttendees.isEmpty()){
        availableTimeForAll.add(TimeRange.WHOLE_DAY);
        return availableTimeForAll;
      }
      if (duration > 60 * 24){
          return availableTimeForAll;
      }

      Set<String> attendeesSet = new HashSet<String>();
      //adding attendees into the set
      for(String attendee: mandatoryAttendees) {
        attendeesSet.add(attendee);
      }
      // availableTimes array set up to have minutes with the boolean value for when the meetings can happen
      // all of the minutes when meetings can be held will be marked true
      boolean[] availableTimes = new boolean[TimeRange.END_OF_DAY+1];
      for(int i = 0; i < availableTimes.length; i++) {
        availableTimes[i] = true;
      }
      
      boolean containsAttendeesInCommon = false;
      for(Event event: events) {
        Collection<String> eventAttendees = event.getAttendees();
        TimeRange when = event.getWhen();
        int eventStart = when.start();
        int eventEnd = when.end();

        for(String eventAttendee: eventAttendees) {
          if(attendeesSet.contains(eventAttendee)) {
            containsAttendeesInCommon = true;
          }
        }

        // check if any of the attendees are attending the event
        if(containsAttendeesInCommon) {
            //marks minutes as false
            for(int i = eventStart; i < eventEnd; i++) {
                availableTimes[i] = false;
            }
        }
      }
      
      int start = 0;
      int current = 0;
      //runs a linear search to search for a availbleTime
      for(int min = 0; min <= TimeRange.END_OF_DAY; min++) {
        if(availableTimes[min]) {
            current++;
        }
        else {
           //If the time range lasts longer than the required duration, add it to the output. 
            if(current >= duration) {
                availableTimeForAll.add(TimeRange.fromStartDuration(start, current));
            }
            start = min+1;
            current = 0;
        }
      }
      if(current >= duration) {
        availableTimeForAll.add(TimeRange.fromStartDuration(start, current));
      }

      return availableTimeForAll;
    }
}