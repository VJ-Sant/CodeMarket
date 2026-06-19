import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/api';
import { useAuth } from '../contexts/AuthContext';
import { Project } from '../types';

export default function SellerProjectsPage() {
  const { user } = useAuth();
  const [projects, setProjects] = useState<Project[]>([]);

  useEffect(() => {
    if (user) {
      fetchProjects();
    }
  }, [user]);

  const fetchProjects = async () => {
    if (!user) return;
    const response = await api.get<{ content: Project[] }>(`/projects/owner/${encodeURIComponent(user.username)}?size=20`);
    setProjects(response.data.content);
  };

  const deleteProject = async (projectId: number) => {
    await api.delete(`/projects/${projectId}`);
    fetchProjects();
  };

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>My Projects</h2>
        <Link className="btn btn-primary" to="/seller/projects/new">
          Create Project
        </Link>
      </div>
      <div className="list-group">
        {projects.map((project) => (
          <div key={project.projectId} className="list-group-item">
            <div className="d-flex justify-content-between align-items-center">
              <div>
                <h5>{project.projectName}</h5>
                <p className="mb-1">{project.categoryName}</p>
                <p className="text-muted mb-0">${project.projectPrice.toFixed(2)}</p>
              </div>
              <div className="btn-group">
                <Link className="btn btn-outline-secondary" to={`/seller/projects/${project.projectId}/edit`}>
                  Edit
                </Link>
                <button className="btn btn-outline-danger" onClick={() => deleteProject(project.projectId)}>
                  Delete
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
