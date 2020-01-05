package si.thoughts.texts.services.beans;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.thoughts.texts.lib.TextMetadata;
import si.thoughts.texts.models.converters.TextMetadataConverter;
import si.thoughts.texts.models.entities.TextMetadataEntity;
import si.thoughts.texts.services.config.IntegrationProperties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@RequestScoped
public class TextMetadataBean {

    private Logger log = Logger.getLogger(TextMetadataBean.class.getName());

    @Inject
    private EntityManager em;

    private Client httpClient;

    @Inject
    @DiscoverService("ratings-service")
    private Optional<String> baseUrl;

    @Inject
    private IntegrationProperties integrationProperties;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
    }

    public List<TextMetadata> getTextMetadata() {

        TypedQuery<TextMetadataEntity> query = em.createNamedQuery("TextMetadataEntity.getAll",
                TextMetadataEntity.class);

        return query.getResultList().stream().map(TextMetadataConverter::toDto).collect(Collectors.toList());

    }

    public List<TextMetadata> getTextMetadataFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, TextMetadataEntity.class, queryParameters).stream()
                .map(TextMetadataConverter::toDto).collect(Collectors.toList());
    }

    public TextMetadata getTextMetadata(Integer id) {

        TextMetadataEntity textMetadataEntity = em.find(TextMetadataEntity.class, id);

        if (textMetadataEntity == null) {
            throw new NotFoundException();
        }

        TextMetadata textMetadata = TextMetadataConverter.toDto(textMetadataEntity);

        if(integrationProperties.isIntegrateWithRatingsService()){
            textMetadata.setNumOfRatings(getRatingCount(id));
        }

        return textMetadata;
    }

    public TextMetadata createTextMetadata(TextMetadata textMetadata) {

        TextMetadataEntity textMetadataEntity = TextMetadataConverter.toEntity(textMetadata);

        try {
            beginTx();
            em.persist(textMetadataEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        if (textMetadataEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return TextMetadataConverter.toDto(textMetadataEntity);
    }

    public TextMetadata putTextMetadata(Integer id, TextMetadata textMetadata) {

        TextMetadataEntity c = em.find(TextMetadataEntity.class, id);

        if (c == null) {
            return null;
        }

        TextMetadataEntity updatedTextMetadataEntity = TextMetadataConverter.toEntity(textMetadata);

        try {
            beginTx();
            updatedTextMetadataEntity.setId(c.getId());
            updatedTextMetadataEntity = em.merge(updatedTextMetadataEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return TextMetadataConverter.toDto(updatedTextMetadataEntity);
    }

    public boolean deleteTextMetadata(Integer id) {

        TextMetadataEntity textMetadata = em.find(TextMetadataEntity.class, id);

        if (textMetadata != null) {
            try {
                beginTx();
                em.remove(textMetadata);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }

    public Integer getRatingCount(Integer textId){
        log.info("Calling rating service: getting rating count");

        if(baseUrl.isPresent()){
            log.info("Address of ratings service is: \n");
            log.info(baseUrl.get());
            try{
                return httpClient
                        .target(baseUrl.get() + "/v1/ratings/count")
                        .queryParam("textId", textId)
                        .request().get(new GenericType<Integer>(){});
            } catch (WebApplicationException | ProcessingException e){
                log.severe(e.getMessage());
                throw new InternalServerErrorException(e);
            }
        }
        return null;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }

    public void loadOrder(Integer n) {


    }
}
