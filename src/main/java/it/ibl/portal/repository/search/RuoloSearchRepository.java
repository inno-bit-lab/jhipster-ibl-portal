package it.ibl.portal.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import it.ibl.portal.domain.Ruolo;
import it.ibl.portal.repository.RuoloRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Ruolo} entity.
 */
public interface RuoloSearchRepository extends ElasticsearchRepository<Ruolo, Long>, RuoloSearchRepositoryInternal {}

interface RuoloSearchRepositoryInternal {
    Stream<Ruolo> search(String query);

    Stream<Ruolo> search(Query query);

    @Async
    void index(Ruolo entity);

    @Async
    void deleteFromIndexById(Long id);
}

class RuoloSearchRepositoryInternalImpl implements RuoloSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final RuoloRepository repository;

    RuoloSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, RuoloRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Ruolo> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<Ruolo> search(Query query) {
        return elasticsearchTemplate.search(query, Ruolo.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Ruolo entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Ruolo.class);
    }
}
