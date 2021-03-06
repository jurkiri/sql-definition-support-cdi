/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details. You should have received a copy of the GNU Lesser General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>
 * 
 *******************************************************************************/
package es.indaba.sqld.annotations.cdi.impl;

import java.lang.annotation.Annotation;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.util.BeanUtils;

import es.indaba.sqld.annotations.cdi.api.QueryResolver;
import es.indaba.sqld.api.QueryDefinition;
import es.indaba.sqld.api.QueryDefinitionRepository;

@ApplicationScoped
public class QueryDefinitionProducer {

    @Produces
    @Dependent
    @QueryResolver("ignore")
    public String produceQueryString(InjectionPoint injectionPoint) {
        QueryResolver queryResolver = getAnnotation(injectionPoint, QueryResolver.class);

        if (queryResolver == null) {
            throw new IllegalStateException("producer method called without @QueryResolver being present!");
        }

        QueryDefinitionRepository repository =
                BeanProvider.getContextualReference(QueryDefinitionRepository.class, false);

        return repository.getQuery(queryResolver.value());

    }

    @Produces
    @Dependent
    @QueryResolver("ignore")
    public QueryDefinition produceQueryDefinition(InjectionPoint injectionPoint) {
        QueryResolver queryResolver = getAnnotation(injectionPoint, QueryResolver.class);

        if (queryResolver == null) {
            throw new IllegalStateException("producer method called without @QueryResolver being present!");
        }

        QueryDefinitionRepository repository =
                BeanProvider.getContextualReference(QueryDefinitionRepository.class, false);

        return new QueryDefinition(queryResolver.value(), repository.getQuery(queryResolver.value()));
    }

    /**
     * @param injectionPoint current injection point
     * @param targetType target type
     * @param <T> type
     * @return annotation instance extracted from the injection point which matches the given type
     */
    protected <T extends Annotation> T getAnnotation(InjectionPoint injectionPoint, Class<T> targetType) {
        return BeanUtils.extractAnnotation(injectionPoint.getAnnotated(), targetType);
    }


}
