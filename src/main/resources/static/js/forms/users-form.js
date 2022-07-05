const App = () => {

    const [waiting, setWaiting] = React.useState(true)

    const [roles, setRoles] = React.useState([])
    const [user, setUser] = React.useState()

    const findAllRoles = React.useCallback(async () => {
        const { data: roles } = await axios
            .get('app/json/roles');

        setRoles(roles);
    }, []);

    const findUser = React.useCallback(async () => {
        const id = Number(window.location.pathname.split('/').pop())

        if (Number.isSafeInteger(Number(id))) {
            const { data: user } = await axios
                .get(`app/json/user/${id}`);

            setUser(user);
            return;
        }

        setUser({
            id: null,
            name: "",
            email: "",
            roles: []
        })
    }, []);

    const fetch = React.useCallback(async () => {
        await Promise.all([findUser(), findAllRoles()]);
        setWaiting(false);
    }, []);


    React.useEffect(() => {
        fetch();
    }, [])

    return !waiting && (
        <div className="col-md-4 shadow-sm p-3 mb-5 bg-body rounded">
            <pre>
                {JSON.stringify(user, undefined, 4)}
            </pre>
            <div className="row">
                <div className="col-md-12">
                    <div className="form-group">
                        <label>
                            <small>
                                Nome
                                <span className="text-danger">
                                    <strong>*</strong>
                                </span>
                            </small>
                        </label>
                        <input
                            required
                            name="name"
                            type="text"
                            value={user.name}
                            className="form-control form-control-sm"
                            onChange={({ target: { value } }) => setUser({
                                ...user,
                                name: value
                            })}
                        />
                    </div>
                </div>
                <div className="col-md-12 mt-3">
                    <div className="form-group">
                        <label>
                            <small>
                                Email
                                <span className="text-danger">
                                    <strong>*</strong>
                                </span>
                            </small>
                        </label>
                        <input
                            required
                            name="email"
                            type="email"
                            value={user.email}
                            className="form-control form-control-sm"
                            onChange={({ target: { value } }) => setUser({
                                ...user,
                                email: value
                            })}
                        />

                        <div className="invalid-feedback">
                            <small>Por favor, informe o numero de andares.</small>
                        </div>
                    </div>
                </div>
                <div className="col-md-12 mt-3">
                    <label>
                        <small>
                            Role
                            <span className="text-danger">
                                <strong>*</strong>
                            </span>
                        </small>
                    </label>
                    <select
                        required
                        name="roles"
                        className="form-select form-select-sm"
                    >
                        <option value="">Selecione</option>
                        {roles.map(({ id, name, initials }) => (
                            <option selected={user.roles[0] == initials} value={id}>{name}</option>
                        ))}
                    </select>

                    <div className="invalid-feedback">
                        <small>Por favor, informe a fonte da coleta.</small>
                    </div>
                </div>
            </div>
            <hr />
            <div className="grid">
                <div className="g-col-6">
                    <a href="app/users" className="btn btn-sm btn-outline-primary">
                        Voltar
                    </a>
                </div>
                <div className="g-col-6">
                    <button className="btn btn-sm btn-success" type="submit">
                        Salvar
                    </button>
                </div>
            </div>
        </div>
    );
}

ReactDOM.render(<App />, document.querySelector('#app'))