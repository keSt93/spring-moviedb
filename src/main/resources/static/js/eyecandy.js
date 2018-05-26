// Progress Bar Stuff --------------------------------------------------------------------------------
function adjustRatingProgressBarColor(elemClass, percentage) {
    $(elemClass).css('background-color', function(){;
        if (percentage > 0 && percentage < 15){
            return '#721414'
        }
        else if (percentage > 14 && percentage < 30){
            return '#a91818'
        }
        else if (percentage > 29 && percentage < 45){
            return '#cb7300'
        }
        else if (percentage > 44 && percentage < 60){
            return '#d19e00'
        }
        else if (percentage > 59 && percentage < 75) {
            return '#bfbc00';
        }
        else if (percentage > 74 && percentage <= 90) {
            return '#7ba01c';
        }
        else if (percentage > 89 && percentage <= 100) {
            return '#3a8d24';
        }
    });
}

function adjustRatingProgressBar(elemClass, percent) {
    $(elemClass).each(function(i) {
        var interval = setInterval(progress, 10);
        var width = 1;
        var that = this;
        var maxwidth = percent;
        function progress() {
            if (width >= maxwidth) {
                clearInterval(interval);
            } else {
                width++;
                that.style.width = width + '%';
                that.innerHTML = width * 1 + '%';
                adjustRatingProgressBarColor(elemClass, width);
            }
        }
    })
}
function adjustRatingProgressBarWithCustomText(elemClass, percent, customText) {
    $(elemClass).each(function(i) {
        var interval = setInterval(progress, 10);
        var width = 1;
        var that = this;
        var maxwidth = percent;
        function progress() {
            if (width >= maxwidth) {
                clearInterval(interval);
            } else {
                width++;
                that.style.width = width + '%';
                that.innerHTML = customText + width * 1 + '%';
                adjustRatingProgressBarColor(elemClass, width);
            }
        }
    })
}

// End Progress Bar Stuff --------------------------------------------------------------------------------

// Number Stuff ------------------------------------------------------------------------------------------
function countNumberUp(elemClass, maxvalue) {
    $(elemClass).each(function(i) {
        var interval = setInterval(progress, 10);
        var value = 0;
        var that = this;
        function progress() {
            if (value >= maxvalue) {
                clearInterval(interval);
            } else {
                value++;
                that.innerHTML = value;
            }
        }
    })
}
function countNumberUpStartPageText(elemClass, maxvalue) {
    $(elemClass).each(function(i) {
        var interval = setInterval(progress, 10);
        var value = 0;
        var that = this;
        function progress() {
            if (value >= maxvalue) {
                clearInterval(interval);
            } else {
                value++;
                that.innerHTML = "mit " + value + " Filmen verschwendet:";
            }
        }
    })
}
// End Number Stuff ------------------------------------------------------------------------------------------

// Background Stuff ------------------------------------------------------------------------------------------
function getRandomBackground() {
    var $body = $("body");
    var images = [
        "/img/background-images/popcorn-1085072_1920.jpg",
        "/img/background-images/movie-918655_1920.jpg",
        "/img/background-images/cinema-2093264_1920.jpg",
    ]
    var imagesCount = images.length;
    var rnd = Math.floor((Math.random() * imagesCount) + 1);
    var imageCSS = "background-image: url('"+ images[rnd-1]+"')"

    $body.attr("style", imageCSS);
}
// End Background Stuff --------------------------------------------------------------------------------------