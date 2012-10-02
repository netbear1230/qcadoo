package com.qcadoo.plugins.customTranslations.internal.listeners;

import static com.qcadoo.customTranslation.constants.CustomTranslationFields.LOCALE;
import static com.qcadoo.plugins.customTranslations.constants.CustomTranslationFieldsCTM.PROPERTIES_TRANSLATION;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.qcadoo.customTranslation.api.CustomTranslationManagementService;
import com.qcadoo.model.api.DataDefinition;
import com.qcadoo.model.api.Entity;
import com.qcadoo.view.api.ComponentState;
import com.qcadoo.view.api.ComponentState.MessageType;
import com.qcadoo.view.api.ViewDefinitionState;
import com.qcadoo.view.api.components.FieldComponent;
import com.qcadoo.view.api.components.FormComponent;

public class ReplaceCustomTranslationsListenersTest {

    private ReplaceCustomTranslationsListeners replaceCustomTranslationsListeners;

    private static final String L_FORM = "form";

    private static final String L_REPLACE_TO = "replaceTo";

    private static final String L_REPLACE_FROM = "replaceFrom";

    @Mock
    private CustomTranslationManagementService customTranslationManagementService;

    @Mock
    private ViewDefinitionState view;

    @Mock
    private ComponentState state;

    @Mock
    private FormComponent replaceCustomTranslationsFrom;

    @Mock
    private FieldComponent localeField, replaceFromField, replaceToField;

    @Mock
    private DataDefinition customTranslationDD;

    @Mock
    private Entity customTranslation;

    @Mock
    private List<Entity> customTranslations;

    @Mock
    private Iterator<Entity> customTranslationsIterator;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        replaceCustomTranslationsListeners = new ReplaceCustomTranslationsListeners();

        given(view.getComponentByReference(L_FORM)).willReturn(replaceCustomTranslationsFrom);

        ReflectionTestUtils.setField(replaceCustomTranslationsListeners, "customTranslationManagementService",
                customTranslationManagementService);
    }

    @Test
    public void shouldReplaceWhenReplaceCustomTranslationsIfCustomTranslationsArentNull() {
        // given
        String locale = "pl";
        String replaceFrom = "replaceFrom";
        String replaceTo = "replaceTo";

        String translation = "replaceFrom";

        given(view.getComponentByReference(LOCALE)).willReturn(localeField);
        given(view.getComponentByReference(L_REPLACE_FROM)).willReturn(replaceFromField);
        given(view.getComponentByReference(L_REPLACE_TO)).willReturn(replaceToField);

        given(localeField.getFieldValue()).willReturn(locale);
        given(replaceFromField.getFieldValue()).willReturn(replaceFrom);
        given(replaceToField.getFieldValue()).willReturn(replaceTo);

        given(customTranslationManagementService.getCustomTranslations(locale)).willReturn(customTranslations);

        given(customTranslationsIterator.hasNext()).willReturn(true, false);
        given(customTranslationsIterator.next()).willReturn(customTranslation);

        given(customTranslations.iterator()).willReturn(customTranslationsIterator);

        given(customTranslation.getStringField(PROPERTIES_TRANSLATION)).willReturn(translation);

        given(customTranslation.getDataDefinition()).willReturn(customTranslationDD);

        // when
        replaceCustomTranslationsListeners.replaceCustomTranslations(view, state, null);

        // then
        verify(customTranslation).setField(Mockito.anyString(), Mockito.any());
        verify(customTranslationDD).save(Mockito.any(Entity.class));
        verify(replaceCustomTranslationsFrom).addMessage(Mockito.anyString(), Mockito.any(MessageType.class));
    }

    @Test
    public void shouldntReplaceWhenReplaceCustomTranslationsIfCustomTranslationsArentNullAndNotFound() {
        // given
        String locale = "pl";
        String replaceFrom = "replaceFrom";
        String replaceTo = "replaceTo";

        String translation = "translation";

        given(view.getComponentByReference(LOCALE)).willReturn(localeField);
        given(view.getComponentByReference(L_REPLACE_FROM)).willReturn(replaceFromField);
        given(view.getComponentByReference(L_REPLACE_TO)).willReturn(replaceToField);

        given(localeField.getFieldValue()).willReturn(locale);
        given(replaceFromField.getFieldValue()).willReturn(replaceFrom);
        given(replaceToField.getFieldValue()).willReturn(replaceTo);

        given(customTranslationManagementService.getCustomTranslations(locale)).willReturn(customTranslations);

        given(customTranslationsIterator.hasNext()).willReturn(true, false);
        given(customTranslationsIterator.next()).willReturn(customTranslation);

        given(customTranslations.iterator()).willReturn(customTranslationsIterator);

        given(customTranslation.getStringField(PROPERTIES_TRANSLATION)).willReturn(translation);

        given(customTranslation.getDataDefinition()).willReturn(customTranslationDD);

        // when
        replaceCustomTranslationsListeners.replaceCustomTranslations(view, state, null);

        // then
        verify(customTranslation, never()).setField(Mockito.anyString(), Mockito.any());
        verify(customTranslationDD, never()).save(Mockito.any(Entity.class));
        verify(replaceCustomTranslationsFrom).addMessage(Mockito.anyString(), Mockito.any(MessageType.class));
    }

    @Test
    public void shouldntReplaceWhenReplaceCustomTranslationsIfCustomTranslationsAreNull() {
        // given
        String locale = "pl";
        String replaceFrom = "replaceFrom";
        String replaceTo = "replaceTo";

        given(view.getComponentByReference(LOCALE)).willReturn(localeField);
        given(view.getComponentByReference(L_REPLACE_FROM)).willReturn(replaceFromField);
        given(view.getComponentByReference(L_REPLACE_TO)).willReturn(replaceToField);

        given(localeField.getFieldValue()).willReturn(locale);
        given(replaceFromField.getFieldValue()).willReturn(replaceFrom);
        given(replaceToField.getFieldValue()).willReturn(replaceTo);

        given(customTranslationManagementService.getCustomTranslations(locale)).willReturn(null);

        // when
        replaceCustomTranslationsListeners.replaceCustomTranslations(view, state, null);

        // then
        verify(customTranslation, never()).setField(Mockito.anyString(), Mockito.any());
        verify(customTranslationDD, never()).save(Mockito.any(Entity.class));
        verify(replaceCustomTranslationsFrom).addMessage(Mockito.anyString(), Mockito.any(MessageType.class));
    }

    @Test
    public void shouldntReplaceWhenReplaceCustomTranslationsIfLocaleIsNullAndReplaceFromIsNull() {
        // given
        given(view.getComponentByReference(LOCALE)).willReturn(localeField);
        given(view.getComponentByReference(L_REPLACE_FROM)).willReturn(replaceFromField);
        given(view.getComponentByReference(L_REPLACE_TO)).willReturn(replaceToField);

        given(localeField.getFieldValue()).willReturn(null);
        given(replaceFromField.getFieldValue()).willReturn(null);
        given(replaceToField.getFieldValue()).willReturn(null);

        // when
        replaceCustomTranslationsListeners.replaceCustomTranslations(view, state, null);

        // then
        verify(customTranslation, never()).setField(Mockito.anyString(), Mockito.any());
        verify(customTranslationDD, never()).save(Mockito.any(Entity.class));
        verify(replaceCustomTranslationsFrom).addMessage(Mockito.anyString(), Mockito.any(MessageType.class));
    }

}