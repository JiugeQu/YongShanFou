function onClick() {
  location.href = '../../pages/delivering_order/delivering_order.html';
}
function back_to_home(){

}
function getName(){
  var myHeaders = new Headers();
  myHeaders.append("token", "<token>");
  myHeaders.append("User-Agent", "Apifox/1.0.0 (https://apifox.com)");

  var requestOptions = {
    method: 'GET',
    headers: myHeaders,
    redirect: 'follow'
  };

  fetch("http://127.0.0.1:4523/m1/3671781-0-default/user/me", requestOptions)
      .then(response => response.json())
      .then(data => {
        const container = document.getElementById('personal_name');
        container.innerText=data.data.name;
      })
      .catch(error => console.log('error', error));
}