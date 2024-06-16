function showSwitchStoreModal() {

    populateCities();
    $("#chooseStoreModal").modal({backdrop: 'static', keyboard: false});
}

function populateCities() {
    var citySelectorURL = null;

    citySelectorURL = storeServiceURL + "/store/cities";
    $("#globalCitySelector").empty();

    $.ajax({
        type: "get",
        url: citySelectorURL,
        async: false,
        dataType: 'json',
        headers: {'Accept':'application/json','Access-Control-Allow-Origin':'*'},
        success: function(data, status) {
            if(status == 'success') {
                $(data).each(function(idx, elem) {
                    var option = document.createElement("option");
                    option.value=elem.cityId;
                    option.label=elem.cityName;
                    $("#globalCitySelector").append(option);
                });
                populateStores();
            }
        },
        error: function(error, status) {
            alert("something went wrong! error: "+ error +" status: " + status);
        }
    });
}

function populateStores() {
    var storeSelectorURL = null;
    var cityId = null;

    cityId = $("#globalCitySelector").val();
    storeSelectorURL = storeServiceURL + "/store/"+cityId;
    $("#globalStoreSelector").empty();
    $.ajax({
         type: "get",
         url: storeSelectorURL,
         async: false,
         dataType: 'json',
         headers: {'Accept':'application/json','Access-Control-Allow-Origin':'*'},
         success: function(data, status) {
             if(status == 'success') {
                 $(data).each(function(idx, elem) {
                     var option = document.createElement("option");
                     option.value=elem.storeId;
                     option.label=elem.registeredBusinessName+ " [ " + elem.storeAreaName+ " ]";
                     $("#globalStoreSelector").append(option);
                 });
             }
         },
         error: function(error, status) {
             alert("something went wrong! error: "+ error +" status: " + status);
         }
     });
}