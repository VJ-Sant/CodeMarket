import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../api/api';
import { Project } from '../types';

export default function CategoryPage() {
  const { name } = useParams();
  const [projects, setProjects] = useState<Project[]>([]);

  useEffect(() => {
    if (name) {
      if (name.toLowerCase() === 'all') {
        fetchAllProjects();
      } else {
        fetchCategoryProjects(name);
      }
    }
  }, [name]);

  const fetchCategoryProjects = async (categoryName: string) => {
    const response = await api.get<{ content: Project[] }>(`/projects/search/category?category=${encodeURIComponent(categoryName)}&size=20`);
    setProjects(response.data.content);
  };

  const fetchAllProjects = async () => {
    const response = await api.get<{ content: Project[] }>('/projects?size=20');
    setProjects(response.data.content);
  };

  return (
    <div>
      <h2>Category: {name}</h2>
      <div className="row gy-4">
        {projects.map((project) => (
          <div className="col-md-4" key={project.projectId}>
            <div className="card h-100">
              <img src={project.thumbnailUrl || 'https://via.placeholder.com/400x220'} className="card-img-top" alt={project.projectName} />
              <div className="card-body d-flex flex-column">
                <h5>{project.projectName}</h5>
                <p className="text-truncate">{project.projectDescription}</p>
                <p className="text-muted mb-2">Price: ${project.projectPrice.toFixed(2)}</p>
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
