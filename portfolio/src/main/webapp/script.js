 $(document).ready(function(){
	$('.header').height($(window).height());
})

function addRandomFact() {
  const facts =
      ['I love making lattes', 'I do concert lighting in my free time', 
      'I love surfing, and water sports', 'I love traveling', 
      'Football and lacrosse is my favorite sports', 
      'I am interested in Developer Relations', 'I love cooking', 
      'I am from Southern California'];

  const fact = facts[Math.floor(Math.random() * facts.length)];

  const factContainer = document.getElementById('fact-container');
  factContainer.innerText =fact;
}