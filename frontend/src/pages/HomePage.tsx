import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/api';
import { Project, Category } from '../types';

export default function HomePage() {
  const [projects, setProjects] = useState<Project[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [search, setSearch] = useState('');

  useEffect(() => {
    fetchProjects();
    fetchCategories();
  }, []);

  const fetchProjects = async () => {
    const response = await api.get<{ content: Project[] }>('/projects?size=12');
    setProjects(response.data.content);
  };

  const fetchCategories = async () => {
    const response = await api.get<Category[]>('/categories');
    setCategories(response.data);
  };

  const searchProjects = async () => {
    if (!search.trim()) return fetchProjects();
    const response = await api.get<{ content: Project[] }>(`/projects/search/name?name=${encodeURIComponent(search)}&size=12`);
    setProjects(response.data.content);
  };

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h1>CodeMarket</h1>
          <p>Browse projects, buy templates, and grow your marketplace.</p>
        </div>
        <div className="input-group w-50">
          <input
            type="text"
            className="form-control"
            placeholder="Search projects..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
          <button className="btn btn-primary" onClick={searchProjects}>
            Search
          </button>
        </div>
      </div>

      <div className="mb-4">
        <h4>Categories</h4>
        <div className="d-flex flex-wrap gap-2">
          <Link className="btn btn-outline-secondary" to="/">
            All
          </Link>
          {categories.map((category) => (
            <Link key={category.id} className="btn btn-outline-secondary" to={`/categories/${encodeURIComponent(category.name)}`}>
              {category.name}
            </Link>
          ))}
        </div>
      </div>

      <div className="row gy-4">
        {projects.map((project) => (
          <div className="col-md-4" key={project.projectId}>
            <div className="card h-100">
              <img src={project.thumbnailUrl || 'https://via.placeholder.com/400x220'} className="card-img-top" alt={project.projectName} />
              <div className="card-body d-flex flex-column">
                <h5 className="card-title">{project.projectName}</h5>
                <p className="card-text text-truncate">{project.projectDescription}</p>
                <p className="text-muted mb-1">Category: {project.categoryName}</p>
                <p className="fw-bold mb-3">${project.projectPrice.toFixed(2)}</p>
                <Link className="btn btn-primary mt-auto" to={`/projects/${project.projectId}`}>
                  View Project
                </Link>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
