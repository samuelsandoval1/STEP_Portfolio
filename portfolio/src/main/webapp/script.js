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

/**
 * Adds a random greeting to the page.
 */
 $(document).ready(function(){
	$('.header').height($(window).height());
})

function funFact() {
  const quotes =
      ['I love making lattes', 'I do concert lighting in my free time', 
      'I love surfing, and water sports', 'I love traveling', 
      'Football and lacrosse is my favorite sports', 
      'I am interested in Developer Relations', 'I love cooking', 
      'I am from Southern California'];

  // Pick a random quote.
  const quote = quotes[Math.floor(Math.random() * quotes.length)];

  // Add it to the page.
  const factContainer = document.getElementById('fact-container');
  factContainer.innerText = quote;
}