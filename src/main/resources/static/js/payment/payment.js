
    var paymentButton = document.getElementById("payment-button").value
    document.getElementById("payment-button").onclick = function(){
    var text = "";
    fetch("http://localhost:8095"+paymentButton)
    .then(res => res.json())
    .then(json => {
        text = json;
        console.log(text.approvalUrl);
        var link = text.approvalUrl;

        location.href=link;
        location.replace(link);
        window.open(link);
    });
    }