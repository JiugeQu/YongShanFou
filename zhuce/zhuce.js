axios.defaults.baseURL = 'http://127.0.0.1:4523/m1/3592222-0-default';

// 获取短信验证码
document.addEventListener('DOMContentLoaded', function() {  
   var button = document.getElementById('button_duanxin');  
   button.addEventListener('click', function() {  
      axios.post('/user/code', null, { 
         params: phone 
      })  
      .then(function (response) {  
         console.log(response.data.msg);
         document.getElementById("code").value = response.data.msg;   
      })  
      .catch(function (error) {  
         console.error(error);  
      });
   });  
});
  
// 注册
var role = sessionStorage.getItem('role'); // 从sessionStorage中取出role值
document.addEventListener('DOMContentLoaded', function() {  
   var button = document.getElementById('button_zhuce');  
   button.addEventListener('click', function() {  
      var queryBody = {
         name: document.getElementById('name').value,
         phone: document.getElementById('phone').value,
         code: document.getElementById('code').value,
         role: role
      };
      axios.post('/user/sign', queryBody)
      .then(function (response) {  
         console.log(response.data); 
      })  
      .catch(function (error) {  
         console.error(error);  
      });
   });  
});