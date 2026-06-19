import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../api/api';
import { Project } from '../types';
import { useAuth } from '../contexts/AuthContext';

export default function ProjectDetailsPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [project, setProject] = useState<Project | null>(null);
  const [message, setMessage] = useState('');
  const { user } = useAuth();

  useEffect(() => {
    if (id) {
      fetchProject(Number(id));
    }
  }, [id]);

  const fetchProject = async (projectId: number) => {
    const response = await api.get<Project>(`/projects/${projectId}`);
    setProject(response.data);
  };

  const handlePurchase = async () => {
    if (!user) {
      navigate('/login');
      return;
    }
    await api.post(`/purchases/${project?.projectId}`);
    setMessage('Purchase completed successfully.');
  };

  const handleFavorite = async () => {
    if (!user) {
      navigate('/login');
      return;
    }
    await api.post(`/favorites/${project?.projectId}`);
    setMessage('Added to favorites.');
  };

  if (!project) return <p>Loading project...</p>;

  return (
    <div>
      <div className="row mb-4">
        <div className="col-md-6">
          <img src={project.thumbnailUrl || 'https://via.placeholder.com/800x450'} className="img-fluid rounded" alt={project.projectName} />
        </div>
        <div className="col-md-6">
          <h1>{project.projectName}</h1>
          <p className="text-muted">Category: {project.categoryName}</p>
          <p className="text-muted">Seller: {project.ownerUsername}</p>
          <h3 className="text-primary">${project.projectPrice.toFixed(2)}</h3>
          <p>{project.projectDescription}</p>
          <p>Technology: {project.technologyStack}</p>
          <p>Difficulty: {project.difficultyLevel}</p>
          <div className="d-flex gap-2">
            <button className="btn btn-success" onClick={handlePurchase}>
              Purchase
            </button>
            <button className="btn btn-outline-secondary" onClick={handleFavorite}>
              Add to Favorites
            </button>
            {user?.username === project.ownerUsername ? (
              <button className="btn btn-outline-primary" onClick={() => navigate(`/seller/projects/${project.projectId}/edit`)}>
                Edit Project
              </button>
            ) : null}
          </div>
          {message ? <div className="alert alert-success mt-3">{message}</div> : null}
        </div>
      </div>
    </div>
  );
}
