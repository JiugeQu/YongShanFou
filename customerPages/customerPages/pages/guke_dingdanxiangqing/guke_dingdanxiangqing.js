axios.defaults.baseURL = 'http://127.0.0.1:4523/m1/3592222-0-default';

window.onload = () => {
  const data = {};
  const $ = window.document.querySelector.bind(window.document);
  // 从 sessionStorage 中读取数据  
  var selectedDishes = sessionStorage.getItem('selectedDishes');  
  var totalPrice = sessionStorage.getItem('totalPrice');  
  // 更新 DOM 元素  
  var selectedDishesElement = document.getElementById('selected-dishes');    
  selectedDishesElement.textContent = selectedDishes;  
  var totalPriceElement = document.getElementById('total-price');  
  totalPriceElement.textContent = '￥' + parseFloat(totalPrice).toFixed(2);  
  var realPriceElement = document.getElementById('real_price');    
  realPriceElement.textContent = '￥' + (parseFloat(totalPrice)+3).toFixed(2); 
  // 取出送达时间
  var storedDeliveryTime = localStorage.getItem('selectedDeliveryTime');
  document.getElementById('time').textContent = storedDeliveryTime;
  // 取出地址和备注
  var storedAddress = localStorage.getItem('address');
  var storedNote = localStorage.getItem('note');
  document.getElementById('address').textContent = storedAddress;
  document.getElementById('note').textContent = storedNote;
  //  // 从 sessionStorage 中读取数据  
  // var nameInput = sessionStorage.getItem('name');  
  // var phoneInput = sessionStorage.getItem('phone'); 
  //  // 更新 DOM 元素  
  // var bookElement = document.getElementById('book');    
  // bookElement.textContent = nameInput + ' ' + phoneInput;  
};

function openTime() {
  var timeWindow = window.open('time.html');
}

function displayDeliveryTime(time) {
  // 在此处处理传回的时间数据，例如将其显示在界面上
  console.log('Selected delivery time: ' + time);
}
