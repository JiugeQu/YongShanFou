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
  // 获取送达时间
  var selectElement = document.getElementById('delivery_time');  
  if (selectElement) {  
    selectElement.addEventListener('change', function() {  
      var selectedValue = selectElement.value;  
      localStorage.setItem('selectedDeliveryTime', selectedValue);  
    });  
  } else {  
    console.log("Element with id 'delivery_time' not found");  
  }  
  //  个人信息 // 从 sessionStorage 中读取数据  
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

// POST 下单
// header参数是token,暂无
document.addEventListener('DOMContentLoaded', function() {  
   var button = document.getElementById('button_xiadan');  
   button.addEventListener('click', function() {  
      // 请求体是购物车数组
      var storedData = JSON.parse(localStorage.getItem('dataKey'));
      var itemName = storedData.dish;
      var itemNumber = storedData.dishCount;
      var queryBody = {
        dish: itemName,
        dishCount: itemNumber
      };
      var address = document.getElementById('address').value;
      var note = document.getElementById('note').value;
      var expectArriveTime = "2023-12-16T10:00:00";
      // 存储地址和备注到本地存储
      localStorage.setItem('address', address);
      localStorage.setItem('note', note);
      
      axios.post('/customer/order', queryBody, 
      {param:address,note,expectArriveTime})
      .then(function (response) {  
         console.log(response.data); 
      })  
      .catch(function (error) {  
         console.error(error);  
      });
   });  
});

