const form = document.querySelector("#sizes_form");

form && form.addEventListener("change", () => {
    form.submit();
});