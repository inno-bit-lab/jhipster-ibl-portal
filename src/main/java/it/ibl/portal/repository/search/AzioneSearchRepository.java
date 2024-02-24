package it.ibl.portal.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import it.ibl.portal.domain.Azione;
import it.ibl.portal.repository.AzioneRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Azione} entity.
 */
public interface AzioneSearchRepository extends ElasticsearchRepository<Azione, Long>, AzioneSearchRepositoryInternal {}

interface AzioneSearchRepositoryInternal {
    Stream<Azione> search(String query);

    Stream<Azione> search(Query query);

    @Async
    void index(Azione entity);

    @Async
    void deleteFromIndexById(Long id);
}

class AzioneSearchRepositoryInternalImpl implements AzioneSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AzioneRepository repository;

    AzioneSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, AzioneRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Azione> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<Azione> search(Query query) {
        return elasticsearchTemplate.search(query, Azione.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Azione entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Azione.class);
    }
}
