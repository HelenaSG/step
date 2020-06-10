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

/**
 * Comment system.
 */

/** Fetch comments from the server and adds them to the DOM.  */
function loadComments() {
  fetch("/list-comments").then(response => response.json()).then((comments) => {
    // Create HTML content.
    const commentListElement = document.getElementById('comment-list');
    comments.forEach((comment) => {
      commentListElement.appendChild(createCmtElement(comment));
    })
  });
}

/** Refresh to desplay desired amount of comments.  */
function refreshComments() {
  var num = document.getElementById('num').value;
  fetch("/list-comments?user-choice="+num).then(response => response.json()).then((comments) => {
    // Create HTML content.
    const commentListElement = document.getElementById('comment-list');
    while (commentListElement.hasChildNodes()) {
      commentListElement.removeChild(commentListElement.lastChild);
    }
    comments.forEach((comment) => {
      commentListElement.appendChild(createCmtElement(comment));
    })
  });
}

/** Tell the server to delete all comments. */
function deleteAll() {
  const request = new Request('/delete-data', {method: 'POST'});
  fetch(request).then(() => {
    // Delet HTML content.
    const commentListElement = document.getElementById('comment-list');
    while (commentListElement.hasChildNodes()) {
      commentListElement.removeChild(commentListElement.lastChild);
    }
  });
}

/** Create an element that represents a comment, including its delete button. */
function createCmtElement(comment) {
  const commentElement = document.createElement('h6');
  commentElement.innerText = comment.content + " - " + comment.name + " ";

  const deleteButtonElement = document.createElement('button');
  deleteButtonElement.innerText = '✘';
  deleteButtonElement.className = "deleteButton";
  deleteButtonElement.addEventListener('click', () => {
    deleteCmt(comment);

    // Remove the comment from the DOM.
    commentElement.remove();
  });

  //commentElement.appendChild(contentElement);
  commentElement.appendChild(deleteButtonElement);
  return commentElement;
}

/** Tell the server to delete a comment. */
function deleteCmt(comment) {
  const params = new URLSearchParams();
  params.append('id', comment.id);
  fetch('/delete-comment', {method: 'POST', body: params});
}

function login() {
  fetch('/login').then(response => response.json()).then((authResponse) => {
    // show comments section if logged in
    var status = authResponse.status;
    if (status == "1" ){
        document.getElementById("comment-section").style.display = "block";
    }
    else if (status == "0" ){
        document.getElementById("comment-section").style.display = "none";
    }
    // create HTML content)
    const statsListElement = document.getElementById('login-status-container');
    statsListElement.innerHTML = authResponse.htmlContent;
  });
}
