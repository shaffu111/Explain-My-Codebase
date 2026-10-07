import { useEffect, useState } from "react";
import axios from "axios";
import "./App.css";

const API = "http://localhost:8080/api";

function App() {
  const [projects, setProjects] = useState([]);
  const [selectedProject, setSelectedProject] = useState(null);

  const [name, setName] = useState("");
  const [description, setDescription] = useState("");

  const [analysis, setAnalysis] = useState([]);
  const [dependencies, setDependencies] = useState({});
  const [search, setSearch] = useState("");
  const [searchResults, setSearchResults] = useState([]);

  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  useEffect(() => {
    loadProjects();
  }, []);

  const loadProjects = async () => {
    try {
      const response = await axios.get(`${API}/projects`);
      setProjects(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  const createProject = async () => {
    if (!name.trim()) {
      setMessage("Project name is required");
      return;
    }

    try {
      await axios.post(`${API}/projects`, {
        name,
        description,
      });

      setName("");
      setDescription("");
      setMessage("Project created successfully");
      loadProjects();
    } catch (error) {
      setMessage("Failed to create project");
    }
  };

  const openProject = async (project) => {
    setSelectedProject(project);
    setLoading(true);
    setMessage("");

    try {
      const [analysisResponse, dependencyResponse] =
        await Promise.all([
          axios.get(`${API}/projects/${project.id}/java-analysis`),
          axios.get(`${API}/projects/${project.id}/dependencies`),
        ]);

      setAnalysis(analysisResponse.data);
      setDependencies(dependencyResponse.data);
    } catch (error) {
      console.error(error);
      setMessage("Could not load project analysis");
    }

    setLoading(false);
  };

  const uploadProject = async (event) => {
    const file = event.target.files[0];

    if (!file || !selectedProject) return;

    const formData = new FormData();
    formData.append("file", file);

    try {
      setMessage("Uploading and analyzing project...");

      await axios.post(
        `${API}/projects/${selectedProject.id}/upload`,
        formData
      );

      setMessage("Project uploaded successfully");

      openProject(selectedProject);
    } catch (error) {
      console.error(error);
      setMessage("Upload failed");
    }
  };

  const performSearch = async () => {
    if (!search.trim() || !selectedProject) return;

    try {
      const response = await axios.get(
        `${API}/projects/${selectedProject.id}/search`,
        {
          params: {
            query: search,
          },
        }
      );

      setSearchResults(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  const goBack = () => {
    setSelectedProject(null);
    setAnalysis([]);
    setDependencies({});
    setSearch("");
    setSearchResults([]);
    setMessage("");
  };

  return (
    <div className="app">

      <header className="navbar">
        <div>
          <h1>Explain My Codebase</h1>
          <p>Java Full Stack Code Analysis Platform</p>
        </div>
      </header>

      {!selectedProject ? (

        <main className="dashboard">

          <section className="create-card">
            <h2>Create Project</h2>

            <input
              type="text"
              placeholder="Project name"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />

            <textarea
              placeholder="Project description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />

            <button onClick={createProject}>
              Create Project
            </button>

            {message && <p className="message">{message}</p>}
          </section>

          <section className="projects-section">
            <h2>Your Projects</h2>

            {projects.length === 0 ? (
              <p>No projects created yet.</p>
            ) : (
              <div className="project-grid">

                {projects.map((project) => (
                  <div className="project-card" key={project.id}>

                    <h3>{project.name}</h3>

                    <p>
                      {project.description ||
                        "No description provided"}
                    </p>

                    <button
                      onClick={() => openProject(project)}
                    >
                      Open Project
                    </button>

                  </div>
                ))}

              </div>
            )}
          </section>

        </main>

      ) : (

        <main className="analysis-page">

          <button className="back-button" onClick={goBack}>
            ← Back to Projects
          </button>

          <div className="project-header">
            <div>
              <h2>{selectedProject.name}</h2>
              <p>{selectedProject.description}</p>
            </div>

            <label className="upload-button">
              Upload ZIP
              <input
                type="file"
                accept=".zip"
                onChange={uploadProject}
                hidden
              />
            </label>
          </div>

          {message && (
            <p className="message">{message}</p>
          )}

          {loading ? (
            <div className="loading">
              Loading project analysis...
            </div>
          ) : (

            <>
              <section className="stats">

                <div className="stat-card">
                  <span>Java Files</span>
                  <strong>{analysis.length}</strong>
                </div>

                <div className="stat-card">
                  <span>Classes</span>
                  <strong>
                    {analysis.reduce(
                      (total, file) =>
                        total + file.classes.length,
                      0
                    )}
                  </strong>
                </div>

                <div className="stat-card">
                  <span>Methods</span>
                  <strong>
                    {analysis.reduce(
                      (total, file) =>
                        total +
                        file.classes.reduce(
                          (count, cls) =>
                            count + cls.methods.length,
                          0
                        ),
                      0
                    )}
                  </strong>
                </div>

                <div className="stat-card">
                  <span>Files with Dependencies</span>
                  <strong>
                    {
                      Object.values(dependencies).filter(
                        (items) => items.length > 0
                      ).length
                    }
                  </strong>
                </div>

              </section>

              <section className="search-section">

                <h2>Search Codebase</h2>

                <div className="search-box">
                  <input
                    type="text"
                    placeholder="Search class or method..."
                    value={search}
                    onChange={(e) =>
                      setSearch(e.target.value)
                    }
                    onKeyDown={(e) => {
                      if (e.key === "Enter") {
                        performSearch();
                      }
                    }}
                  />

                  <button onClick={performSearch}>
                    Search
                  </button>
                </div>

                {searchResults.length > 0 && (
                  <div className="search-results">

                    {searchResults.map((result, index) => (
                      <div
                        className="result"
                        key={index}
                      >
                        {result}
                      </div>
                    ))}

                  </div>
                )}

              </section>

              <section className="analysis-section">

                <h2>Java Code Analysis</h2>

                {analysis.map((file, index) => (

                  <div className="file-card" key={index}>

                    <h3>{file.classes[0]?.name || "Java File"}</h3>

                    <p className="package">
                      Package: {file.packageName || "default"}
                    </p>

                    {file.classes.map((cls, classIndex) => (

                      <div
                        className="class-info"
                        key={classIndex}
                      >

                        <div className="info-block">
                          <h4>Fields</h4>

                          {cls.fields.length === 0 ? (
                            <p>None</p>
                          ) : (
                            cls.fields.map((field, i) => (
                              <div className="item" key={i}>
                                {field.modifiers.join(" ")}{" "}
                                {field.type} {field.name}
                              </div>
                            ))
                          )}
                        </div>

                        <div className="info-block">
                          <h4>Constructors</h4>

                          {cls.constructors.length === 0 ? (
                            <p>None</p>
                          ) : (
                            cls.constructors.map(
                              (constructor, i) => (
                                <div
                                  className="item"
                                  key={i}
                                >
                                  {constructor.name}(
                                  {constructor.parameters.join(
                                    ", "
                                  )}
                                  )
                                </div>
                              )
                            )
                          )}
                        </div>

                        <div className="info-block">
                          <h4>Methods</h4>

                          {cls.methods.length === 0 ? (
                            <p>None</p>
                          ) : (
                            cls.methods.map((method, i) => (
                              <div
                                className="item"
                                key={i}
                              >
                                {method.modifiers.join(" ")}{" "}
                                {method.returnType}{" "}
                                <strong>
                                  {method.name}
                                </strong>
                                (
                                {method.parameters.join(
                                  ", "
                                )}
                                )
                              </div>
                            ))
                          )}
                        </div>

                      </div>

                    ))}

                  </div>

                ))}

              </section>

              <section className="dependency-section">

                <h2>Dependencies</h2>

                {Object.entries(dependencies).map(
                  ([file, imports]) => (

                    <div
                      className="dependency-card"
                      key={file}
                    >
                      <strong>{file}</strong>

                      {imports.length === 0 ? (
                        <span>No imports</span>
                      ) : (
                        imports.map((item, index) => (
                          <span key={index}>
                            {item}
                          </span>
                        ))
                      )}
                    </div>

                  )
                )}

              </section>

            </>
          )}

        </main>

      )}

    </div>
  );
}

export default App;