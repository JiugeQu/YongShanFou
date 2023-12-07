
function previewImage(event) {
    var input = event.target;
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function () {
            var preview = document.getElementById('previewImage');
            preview.src = reader.result;
        };
        reader.readAsDataURL(input.files[0]);
    }
}


function submitForm() {
    var myHeaders = new Headers();
    myHeaders.append("token", "APS-EWOW8DxoX9mxO0juPIIBzNHVTKyu6ONd");
    myHeaders.append("User-Agent", "Apifox/1.0.0 (https://apifox.com)");

    var formData = new FormData();
    formData.append('name', document.getElementById('name').value);
    formData.append('description', document.getElementById('description').value);
    formData.append('type', document.getElementById('type').value);
    formData.append('price', document.getElementById('price').value);

    // Append the image file to the FormData object
    var imageInput = document.getElementById('image');
    formData.append('image', imageInput.files[0]);

//     var requestOptions = {
//         method: 'POST',
//         headers: myHeaders,
//         body: formData,
//         redirect: 'follow'
//     };

//     // Replace the URL with your actual backend API endpoint
//     fetch("http://127.0.0.1:4523/m1/3576146-0-default/staff/dish", requestOptions)
//         .then(response => response.text())
//         .then(result => console.log(result))
//         .catch(error => console.log('error', error));
// }

axios.post("http://127.0.0.1:4523/m1/3576146-0-default/staff/dish", {
    headers: {
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoxLCJwaG9uZSI6IjE5ODI5NjY5MzUwIiwibmFtZSI6Im1pem9yZSIsImlkIjoxLCJleHAiOjE2OTkyNDkzOTR9.ph5wDGEcHrina_hFthrER_1noKl_ifopEeOFZZPDQx0",
        // "User-Agent": "Apifox/1.0.0 (https://apifox.com)"
    }
})

.then(response => console.log(response.data))
.catch(error => console.log('error', error));
}



  






