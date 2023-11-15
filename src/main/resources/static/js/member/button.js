function submitevent(){

    const trans = $('.submitBtn');

    const check = $('.checkBtn');

    if (trans.hasClass('hidden')){
        trans.addClass('hidden')
        check.removeClass('hidden')
    }

}