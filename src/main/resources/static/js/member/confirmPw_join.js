$(function(){
    $('#memberPassword').keyup(function(){
    $('#checkMessage').html('');
    });

    $('#confirmPassword').keyup(function(){

        if($('#memberPassword').val() != $('#confirmPassword').val()){
          $('#checkMessage').html('비밀번호 일치하지 않음<br><br>');
          $('#checkMessage').attr('color', 'red');
        } else{
          $('#checkMessage').html('비밀번호 일치함<br><br>');
          $('#checkMessage').attr('color', 'green');
        }
    });
});