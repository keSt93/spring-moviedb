function adjustRatingProgressBars() {
    $(".movie-container__rating-progress").each(function(i) {
        var interval = setInterval(progress, 10);
        var width = 1;
        var that = this;
        function progress() {
            if (width >= 100) {
                clearInterval(interval);
            } else {
                width++;
                that.style.width = width + '%';
                that.innerHTML = width * 1 + '%';
                adjustRatingProgressBarColor(width);
            }
        }

        function adjustRatingProgressBarColor(percentage) {
            console.log(percentage)
            $('.movie-container__rating-progress').css('background-color', function(){;
                if (percentage > 0 && percentage < 25){
                    return '#a41818'
                }
                else if (percentage > 24 && percentage < 50) {
                    return '#87581c';
                }
                else if (percentage > 49 && percentage < 75) {
                    return '#997815';
                }
                else if (percentage > 74 && percentage < 90) {
                    return '#7ba01c';
                }
                else if (percentage > 89 && percentage <= 100) {
                    return '#3a8d24';
                }
            });
        }
    })
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

        function adjustRatingProgressBarColor(elemClass, percentage) {
            $(elemClass).css('background-color', function(){;
                if (percentage > 0 && percentage < 25){
                    return '#a41818'
                }
                else if (percentage > 24 && percentage < 50) {
                    return '#87581c';
                }
                else if (percentage > 49 && percentage < 75) {
                    return '#997815';
                }
                else if (percentage > 74 && percentage < 90) {
                    return '#7ba01c';
                }
                else if (percentage > 89 && percentage <= 100) {
                    return '#3a8d24';
                }
            });
        }
    })
}


