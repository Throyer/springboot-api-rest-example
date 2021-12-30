document.querySelector("#delete-confirm")
    .addEventListener("show.bs.modal", ({ target: modal, relatedTarget: button }) => {
        const { id, name } = button.dataset;

        modal.querySelector("h5 > span").innerHTML = name;
        modal.querySelector("form").action = `/app/users/delete/${id}`;
    });