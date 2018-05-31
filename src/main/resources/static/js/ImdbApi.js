function findMovie(movieName) {
    const APIKEY = "92857ebb";
    const INITIAL_DATAURL = "http://www.omdbapi.com/?apikey=" + APIKEY + "&";

    $.get(INITIAL_DATAURL + "s=" + movieName, function (data) {
        var obj = data["Search"];

        var htmloptions = "";
        $(obj).each(function (index, el) {
            htmloptions += '<option value="imdbID: ' + el['imdbID'] + '">' + el['Title'] + '(' + el['Year'] + ') </option>'

            if (index == obj.length - 1) {
                $('#movieTitleListStuff').append(htmloptions);
            } else {
                console.log(index);
            }
        });


    });
}