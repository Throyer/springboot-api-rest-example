document.querySelector('input[name=first]')
    .addEventListener('paste', (event) => {

        /**
         * @type {string}
         */
        const paste = (event.clipboardData || window.clipboardData).getData('text');
        
        const [first, second, third, fourth] = paste.split('');

        document.querySelector('input[name=first]').value = first;
        document.querySelector('input[name=second]').value = second;
        document.querySelector('input[name=third]').value = third;
        document.querySelector('input[name=fourth]').value = fourth;

        document.querySelector('button[type=submit]').focus();
    })