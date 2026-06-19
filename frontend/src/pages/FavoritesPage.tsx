import { useEffect, useState } from 'react';
import api from '../api/api';
import { Project } from '../types';
import { Link } from 'react-router-dom';

export default function FavoritesPage() {
  const [favorites, setFavorites] = useState<Project[]>([]);

  useEffect(() => {
    fetchFavorites();
  }, []);

  const fetchFavorites = async () => {
    const response = await api.get<{ content: Project[] }>('/favorites?size=20');
    setFavorites(response.data.content);
  };

  const removeFavorite = async (projectId: number) => {
    await api.delete(`/favorites/${projectId}`);
    fetchFavorites();
  };

  return (
    <div>
      <h2>Favorites</h2>
      <div className="row gy-4">
        {favorites.map((project) => (
          <div className="col-md-4" key={project.projectId}>
            <div className="card h-100">
              <img src={project.thumbnailUrl || 'https://via.placeholder.com/400x220'} className="card-img-top" alt={project.projectName} />
              <div className="card-body d-flex flex-column">
                <h5>{project.projectName}</h5>
                <p className="text-truncate">{project.projectDescription}</p>
                <Link className="btn btn-primary mt-auto" to={`/projects/${project.projectId}`}>
                  View
                </Link>
                <button className="btn btn-link text-danger mt-2" onClick={() => removeFavorite(project.projectId)}>
                  Remove
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
