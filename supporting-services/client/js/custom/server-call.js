var docUrl = "http://0.0.0.0:52204/Hashgraph/1.0.0/documents";
var docPostUrl = "http://0.0.0.0:52204/Hashgraph/1.0.0/document";

 function getDocuments() {
    console.log("hi there")
    $.get(docUrl, function(data, status){
        var html = "<ul>";
        for(var i=0; i<data.hash.length; i++) {
            html += "<li>"+data.hash[i]+"</li>"
        }
        $( "#data" ).html( html+"</ul>" );
    });
 }

 function postDocuments(hash) {
 console.log("what is the hash:"+hash);

$.ajax({

    url: docPostUrl,
    type:'POST',
    data: '{"hash":"' + hash + '"}',
    dataType: 'json',
    contentType: "application/json; charset=utf-8",
    success:function(res){
        console.log(res);

    },
    error:function(res){
        alert(res.statusText);
    }
});
 }