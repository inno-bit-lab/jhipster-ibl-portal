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

describe('Ruolo e2e test', () => {
  const ruoloPageUrl = '/ruolo';
  const ruoloPageUrlPattern = new RegExp('/ruolo(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const ruoloSample = {};

  let ruolo;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/ruolos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ruolos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ruolos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (ruolo) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ruolos/${ruolo.id}`,
      }).then(() => {
        ruolo = undefined;
      });
    }
  });

  it('Ruolos menu should load Ruolos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ruolo');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Ruolo').should('exist');
    cy.url().should('match', ruoloPageUrlPattern);
  });

  describe('Ruolo page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ruoloPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Ruolo page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/ruolo/new$'));
        cy.getEntityCreateUpdateHeading('Ruolo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ruoloPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ruolos',
          body: ruoloSample,
        }).then(({ body }) => {
          ruolo = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ruolos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [ruolo],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(ruoloPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Ruolo page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ruolo');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ruoloPageUrlPattern);
      });

      it('edit button click should load edit Ruolo page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Ruolo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ruoloPageUrlPattern);
      });

      it('edit button click should load edit Ruolo page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Ruolo');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ruoloPageUrlPattern);
      });

      it('last delete button click should delete instance of Ruolo', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('ruolo').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ruoloPageUrlPattern);

        ruolo = undefined;
      });
    });
  });

  describe('new Ruolo page', () => {
    beforeEach(() => {
      cy.visit(`${ruoloPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Ruolo');
    });

    it('should create an instance of Ruolo', () => {
      cy.get(`[data-cy="created"]`).type('2024-02-24');
      cy.get(`[data-cy="created"]`).blur();
      cy.get(`[data-cy="created"]`).should('have.value', '2024-02-24');

      cy.get(`[data-cy="modified"]`).type('2024-02-23');
      cy.get(`[data-cy="modified"]`).blur();
      cy.get(`[data-cy="modified"]`).should('have.value', '2024-02-23');

      cy.get(`[data-cy="nomeAzione"]`).type('boldly operation solemnly');
      cy.get(`[data-cy="nomeAzione"]`).should('have.value', 'boldly operation solemnly');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        ruolo = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', ruoloPageUrlPattern);
    });
  });
});
