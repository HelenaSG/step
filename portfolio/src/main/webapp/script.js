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
 * Adds a random fact to the page.
 */
var current = 0;

function addRandomFact() {
  const facts =
      ['I love watching horror movies with my friends.', 
      'I have a dance crew of five💃🏻, and our team name is Osmosis!', 
      'I\'m a Pisces♓️ - the dreamer and artist of the zodiac.', 
      'I joined the theatre as a light-board operator.', 
      'I\'m obsessed with crystals.',
      'I like reading detective stories.',
      'I empathize with others very easily.',
      'I have four skateboards so far and can do a bit longboard dancing🛹',
      'My favorite fruit is 🍓🍓🍓'];

  // Pick a random fact.
  var update = Math.floor(Math.random() * facts.length);

  if (update == current) {
      while (update == current) {
          update = Math.floor(Math.random() * facts.length);
      }
  }
  current = update;
  const fact = facts[current];

  // Add it to the page.
  const factContainer = document.getElementById('fact-container');
  factContainer.innerText = fact;
}

//step1
// function getHelloUsingArrowFunctions() {
//   fetch('/data').then(response => response.text()).then((quote) => {
//     document.getElementById('hello-container').innerHTML  = quote;
//   });
// }

//step3
function getServerStats() {
  fetch('/data').then(response => response.json()).then((array) => {
    // create HTML content
    const statsListElement = document.getElementById('server-stats-container');
    statsListElement.innerHTML = "<p>"+array[0]+" "+array[1]+" "+array[2]+"</p>";
  });
}
