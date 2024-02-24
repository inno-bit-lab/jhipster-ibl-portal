import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Azione e2e test', () => {
  const azionePageUrl = '/azione';
  const azionePageUrlPattern = new RegExp('/azione(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const azioneSample = {};

  let azione;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/aziones+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/aziones').as('postEntityRequest');
    cy.intercept('DELETE', '/api/aziones/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (azione) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/aziones/${azione.id}`,
      }).then(() => {
        azione = undefined;
      });
    }
  });

  it('Aziones menu should load Aziones page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('azione');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Azione').should('exist');
    cy.url().should('match', azionePageUrlPattern);
  });

  describe('Azione page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(azionePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Azione page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/azione/new$'));
        cy.getEntityCreateUpdateHeading('Azione');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', azionePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/aziones',
          body: azioneSample,
        }).then(({ body }) => {
          azione = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/aziones+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [azione],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(azionePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Azione page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('azione');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', azionePageUrlPattern);
      });

      it('edit button click should load edit Azione page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Azione');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', azionePageUrlPattern);
      });

      it('edit button click should load edit Azione page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Azione');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', azionePageUrlPattern);
      });

      it('last delete button click should delete instance of Azione', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('azione').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', azionePageUrlPattern);

        azione = undefined;
      });
    });
  });

  describe('new Azione page', () => {
    beforeEach(() => {
      cy.visit(`${azionePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Azione');
    });

    it('should create an instance of Azione', () => {
      cy.get(`[data-cy="created"]`).type('2024-02-24');
      cy.get(`[data-cy="created"]`).blur();
      cy.get(`[data-cy="created"]`).should('have.value', '2024-02-24');

      cy.get(`[data-cy="modified"]`).type('2024-02-23');
      cy.get(`[data-cy="modified"]`).blur();
      cy.get(`[data-cy="modified"]`).should('have.value', '2024-02-23');

      cy.get(`[data-cy="nomeAzione"]`).type('who wholly once');
      cy.get(`[data-cy="nomeAzione"]`).should('have.value', 'who wholly once');

      cy.get(`[data-cy="descrizione"]`).type('mmm daily sweetly');
      cy.get(`[data-cy="descrizione"]`).should('have.value', 'mmm daily sweetly');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        azione = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', azionePageUrlPattern);
    });
  });
});
