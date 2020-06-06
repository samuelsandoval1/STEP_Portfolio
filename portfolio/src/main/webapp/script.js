$(document).ready(function() {
    $('.header').height($(window).height());
})

function addRandomFact() {
    const facts =
        ['I love making lattes',
            ' I do concert lighting in my free time',
            'I love surfing, and water sports',
            'I love traveling',
            'Football and lacrosse is my favorite sports',
            'I am interested in Developer Relations',
            'I love cooking',
            'I am from Southern California'];

    const fact = facts[Math.floor(Math.random() * facts.length)];

    const factContainer = document.getElementById('fact-container');
    factContainer.innerText = fact;
}

//Typewriting Effect
{

    //  * @param {any} element This parameter is always needed.
    //  * @param {any} toRotate This parameter is always needed.
    //  * @param {number} period This paramter is always needed.

    let textBeingTyped = function(element, toRotate, period) {
        this.toRotate = toRotate;
        this.element = element;
        this.loopNum = 0;
        this.period = parseInt(period, 10) || 2000;
        this.text = '';
        this.tick();
        this.isDeleting = false;
    };

    textBeingTyped.prototype.tick = function() {
        let i = this.loopNum % this.toRotate.length;
        let fullTxt = this.toRotate[i];

        if (this.isDeleting) {
            this.text = fullTxt.substring(0, this.text.length - 1);
        } else {
            this.text = fullTxt.substring(0, this.text.length + 1);
        }

        this.element.innerHTML = '<span class="wrap">' + this.text + '</span>';

        let that = this;
        let delta = 200 - Math.random() * 100;

        if (this.isDeleting) { delta /= 2; }

        if (!this.isDeleting && this.text === fullTxt) {
            delta = this.period;
            this.isDeleting = true;
        } else if (this.isDeleting && this.text === '') {
            this.isDeleting = false;
            this.loopNum++;
            delta = 500;
        }

        setTimeout(function() {
            that.tick();
        }, delta);
    };


    window.onload = function() {
        let elements = document.getElementsByClassName('typewrite');
        for (let element of elements) {
            const toRotate = element.getAttribute('data-type');
            const period = element.getAttribute('data-period');
            if (toRotate) {
                new textBeingTyped(element, JSON.parse(toRotate), period);
            }
        }
        loadHome();
        drawChart();
    };
}
/** Display the comments acquired from the datastore */
function loadComments(comments) {
    const commentContainer = document.getElementById("comments-list");
    comments.forEach(commentObject => {
        const childDiv = document.createElement("div");
        childDiv.innerText = "[" + commentObject.timestamp + "] "
            + commentObject.email + ": " + commentObject.comment;
        commentContainer.appendChild(childDiv);

    })
}

/** Acquire a comment from /data and display it. */
async function getComments() {
    const response = await fetch('/data');
    const data = await response.json();
    loadComments(data);
}

/** Delete all comments from datastore. */
async function deleteComments() {
    const response = await fetch('/delete-data', {
        method: 'POST',
    });
    getComments();
}

/** Chart Feature */


google.charts.load('current', { 'packages': ['corechart'] });
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
    fetch('/poll-data').then(response => response.json())
        .then((bigfootSightings) => {
            const data = new google.visualization.DataTable();
            data.addColumn('string', 'Year');
            data.addColumn('number', 'Sightings');
            Object.keys(bigfootSightings).forEach((year) => {
                data.addRow([year, bigfootSightings[year]]);
            });

            const options = {
                'title': 'Bigfoot Sightings',
                'width': 400,
                'height': 200
            };

            const chart = new google.visualization.LineChart(
                document.getElementById('chart-container'));
            chart.draw(data, options);
        });
}

/** Authentication Feature */
async function loadHome() {
    const inputForm = document.getElementById("input-form");
    const logging = document.getElementById("logging");
    const link = document.getElementById("login-link");

    await getComments();
    const logStatus = await getLogStatus();

    if (logStatus.Bool === "true") {
        link.href = logStatus.Url;
        link.innerHTML = "Logout";
        logging.style.display = "block";
        inputForm.style.display = "block";
    } else {
        link.href = logStatus.Url;
        link.innerHTML = "Login";
        logging.style.display = "block";
    }
}

async function getLogStatus() {
    const response = await fetch('/userapi');
    const isLoggedIn = await response.json();
    return isLoggedIn;
}