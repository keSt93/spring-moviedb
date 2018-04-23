// Progress Bar Stuff --------------------------------------------------------------------------------
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

function instantlyAdjustRatingProgressBar(elemClass, percent) {
    $(elemClass).each(function(i) {
        var that = this;
        var maxwidth = percent;

        that.style.width = maxwidth + '%';
        that.innerHTML = maxwidth + '%';
        adjustRatingProgressBarColor(elemClass, maxwidth);

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
// End Progress Bar Stuff --------------------------------------------------------------------------------

// Number Stuff --------------------------------------------------------------------------------
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