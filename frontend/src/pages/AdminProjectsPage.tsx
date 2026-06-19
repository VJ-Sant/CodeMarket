import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/api';
import { Project } from '../types';

export default function AdminProjectsPage() {
  const [projects, setProjects] = useState<Project[]>([]);

  useEffect(() => {
    fetchProjects();
  }, []);

  const fetchProjects = async () => {
    const response = await api.get<{ content: Project[] }>('/projects?size=100');
    setProjects(response.data.content);
  };

  const deleteProject = async (projectId: number) => {
    await api.delete(`/projects/${projectId}`);
    fetchProjects();
  };

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Admin Projects</h2>
        <Link className="btn btn-primary" to="/seller/projects/new">
          Create Project
        </Link>
      </div>
      <div className="table-responsive">
        <table className="table table-hover">
          <thead>
            <tr>
              <th>Name</th>
              <th>Category</th>
              <th>Seller</th>
              <th>Price</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {projects.map((project) => (
              <tr key={project.projectId}>
                <td>{project.projectName}</td>
                <td>{project.categoryName}</td>
                <td>{project.ownerUsername}</td>
                <td>${project.projectPrice.toFixed(2)}</td>
                <td>
                  <Link className="btn btn-sm btn-outline-secondary me-2" to={`/seller/projects/${project.projectId}/edit`}>
                    Edit
                  </Link>
                  <button className="btn btn-sm btn-outline-danger" onClick={() => deleteProject(project.projectId)}>
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
