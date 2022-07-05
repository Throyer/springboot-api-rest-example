const App = () => {

    const [roles, setRoles] = React.useState([])

    const findAllRoles = React.useCallback(async () => {
        const { data: roles } = await axios
            .get('app/roles');

        setRoles(roles);
    }, []);


    React.useEffect(() => {
        findAllRoles();
    }, [])


    return (
        <div className="col-md-4 shadow-sm p-3 mb-5 bg-body rounded">
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
                            className="form-control form-control-sm"
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
                            name="email"
                            type="email"
                            className="form-control form-control-sm"
                            required
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
                        {roles.map(({ id, name }) => (
                            <option value={id}>{name}</option>
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