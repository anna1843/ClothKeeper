const DEFAULT_HEIGHT = 300; // textarea 기본 height

const $textarea = document.querySelector('#contentText');

$textarea.oninput = (event) => {
  const $target = event.target;

  $target.style.height = 0;
  $target.style.height = DEFAULT_HEIGHT + $target.scrollHeight + 'px';
};